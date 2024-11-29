/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract.AlarmControllerState
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.AlarmInteractor.AlarmInteractorEvent
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract.AlarmRepository
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract.SettingsRepository
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmInteractorSpec {
    private val alarmControllerMock: AlarmControllerContract = mockk(relaxed = true)
    private val alarmRepositoryMock: AlarmRepository = mockk(relaxed = true)
    private val settingsRepositoryMock: SettingsRepository = mockk(relaxed = true)
    private val alarmSchedulerMock: AlarmSchedulerContract = mockk(relaxed = true)
    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var testScope: CoroutineScope

    private lateinit var alarmInteractor: AlarmInteractor

    @Before
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()
        testScope = TestScope(testDispatcher)

        Dispatchers.setMain(testDispatcher)

        alarmInteractor = AlarmInteractor(
            alarmControllerMock,
            alarmSchedulerMock,
            alarmRepositoryMock,
            settingsRepositoryMock,
            testDispatcher,
            testScope,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `When startAlarm is called, it begins collecting the AlarmControllerState`() = runTest {
        // Given
        val controllerStateMock = MutableStateFlow(AlarmControllerState.Initial)

        every {
            alarmControllerMock.alarmControllerState
        } returns
            controllerStateMock

        assertEquals(0, controllerStateMock.subscriptionCount.value)

        // When
        alarmInteractor.startAlarm(scope = backgroundScope)

        // Then
        assertEquals(1, controllerStateMock.subscriptionCount.value)
    }

    @Test
    fun `When the ControllerState is updated to ServiceConnected, it loads the alarm and settings from the repository`() =
        runTest {
            // Given
            val controllerStateMock = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)
            val givenAlarmId = 3L

            every {
                alarmControllerMock.alarmControllerState
            } returns
                controllerStateMock

            every {
                alarmRepositoryMock.getAlarm(any(), any())
            } returns
                CompletableDeferred(Result.failure(IllegalStateException()))

            every {
                settingsRepositoryMock.getSettings()
            } returns
                flowOf(Result.failure(IllegalStateException()))

            alarmInteractor.startAlarm(backgroundScope)

            // When
            controllerStateMock.update {
                AlarmControllerState.ServiceConnected(givenAlarmId)
            }

            // Then
            @Suppress("DeferredResultUnused")
            verify { alarmRepositoryMock.getAlarm(givenAlarmId, any()) }
            verify { settingsRepositoryMock.getSettings() }
        }

    @Test
    fun `When the ControllerState is ServiceConnected and the alarm is loaded, it calls AlarmController startAlarm`() =
        runTest {
            // Given
            val controllerStateMock = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)

            every {
                alarmControllerMock.alarmControllerState
            } returns
                controllerStateMock

            setupRepositoriesMock()

            alarmInteractor.startAlarm(backgroundScope)

            // When
            controllerStateMock.update {
                AlarmControllerState.ServiceConnected(fakeAlarm.id)
            }

            // Then
            verify {
                alarmControllerMock
                    .startAlarm(fakeAlarm.ringtone, fakeAlarm.alertType)
            }
        }

    @Test
    fun `When the ControllerState is ServiceConnected and the alarm is loaded, it emits the label`() =
        runTest {
            // Given
            val controllerStateMock = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)

            every {
                alarmControllerMock.alarmControllerState
            } returns
                controllerStateMock

            setupRepositoriesMock()

            alarmInteractor.startAlarm(backgroundScope)

            alarmInteractor.label.test {
                // When
                controllerStateMock.update {
                    AlarmControllerState.ServiceConnected(fakeAlarm.id)
                }

                // Then
                assertEquals(fakeAlarm.label, awaitItem().getOrNull())
            }
        }

    @Test
    fun `When the ControllerState is ServiceConnected and the alarm is not loaded, it calls AlarmController stopService`() =
        runTest {
            // Given
            val controllerStateMock = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)

            every {
                alarmControllerMock.alarmControllerState
            } returns
                controllerStateMock

            every {
                alarmRepositoryMock.getAlarm(any(), any())
            } returns
                CompletableDeferred(Result.failure(IllegalStateException()))

            every {
                settingsRepositoryMock.getSettings()
            } returns
                flow { emit(Result.failure(IllegalStateException())) }

            alarmInteractor.startAlarm(backgroundScope)

            // When
            controllerStateMock.update {
                AlarmControllerState.ServiceConnected(fakeAlarm.id)
            }

            // Then
            verify { alarmControllerMock.stopService() }
        }

    @Test
    fun `When the ControllerState is updated to AudioPlayerError, it emits ERROR_AUDIO_PLAYER event`() = runTest {
        // Given
        val controllerStateMock = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)

        every {
            alarmControllerMock.alarmControllerState
        } returns
            controllerStateMock

        setupRepositoriesMock()
        alarmInteractor.startAlarm(this.backgroundScope)

        alarmInteractor.event.test {
            // When
            controllerStateMock.update {
                AlarmControllerState.AudioPlayerError("")
            }

            // Then
            assertEquals(AlarmInteractorEvent.ERROR_AUDIO_PLAYER, awaitItem())
        }
    }

    @Test
    fun `When the ControllerState is updated to ServiceDisconnected, it emits ERROR_SERVICE_DISCONNECTED event`() =
        runTest {
            // Given
            val controllerStateMock = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)

            every {
                alarmControllerMock.alarmControllerState
            } returns
                controllerStateMock

            setupRepositoriesMock()
            alarmInteractor.startAlarm(backgroundScope)

            alarmInteractor.event.test {
                // When
                controllerStateMock.update {
                    AlarmControllerState.ServiceDisconnected
                }

                // Then
                assertEquals(AlarmInteractorEvent.ERROR_SERVICE_DISCONNECTED, awaitItem())
            }
        }

    @Test
    fun `When the ControllerState is updated to ServiceStateCollectionError, it emits ERROR_SERVICE_DISCONNECTED event`() =
        runTest {
            // Given
            val controllerStateMock = MutableStateFlow<AlarmControllerState>(AlarmControllerState.Initial)

            every {
                alarmControllerMock.alarmControllerState
            } returns
                controllerStateMock

            setupRepositoriesMock()
            alarmInteractor.startAlarm(backgroundScope)
            alarmInteractor.event.test {
                // When
                controllerStateMock.update {
                    AlarmControllerState.ServiceStateCollectionError
                }

                // Then
                assertEquals(AlarmInteractorEvent.ERROR_SERVICE_DISCONNECTED, awaitItem())
            }
        }

    @Test
    fun `When snooze is called, it calls AlarmScheduler scheduleSnooze and AlarmController stopAlarm`() = runTest {
        // Given
        val controllerStateMock = MutableStateFlow(AlarmControllerState.ServiceConnected(fakeAlarm.id))

        every {
            alarmControllerMock.alarmControllerState
        } returns
            controllerStateMock

        setupRepositoriesMock()
        alarmInteractor.startAlarm(backgroundScope)

        // When
        alarmInteractor.snooze()

        // Then
        coVerifyOrder {
            alarmSchedulerMock.scheduleSnooze(fakeAlarm.id, fakeSettings.snooze)
            alarmControllerMock.stopService()
        }
    }

    @Test
    fun `When snooze is called and Settings is not loaded, it sets snooze to 5`() = runTest {
        // Given
        val controllerStateMock = MutableStateFlow(AlarmControllerState.ServiceConnected(fakeAlarm.id))

        every {
            alarmControllerMock.alarmControllerState
        } returns
            controllerStateMock

        every {
            alarmRepositoryMock.getAlarm(any(), any())
        } returns
            CompletableDeferred(Result.success(fakeAlarm))

        every {
            settingsRepositoryMock.getSettings()
        } returns
            flowOf(Result.failure(IllegalStateException()))

        alarmInteractor.startAlarm(backgroundScope)

        // When
        alarmInteractor.snooze()

        // Then
        coVerifyOrder {
            alarmSchedulerMock.scheduleSnooze(any(), Snooze(5))
            alarmControllerMock.stopService()
        }
    }

    @Test
    fun `When stopAlarm is called, it calls AlarmController stopAlarm`() {
        // When
        alarmInteractor.stopAlarm()

        // Then
        verify { alarmControllerMock.stopAlarm() }
    }

    @Test
    fun `It fulfills AlarmInteractorContract`() {
        assertIs<InteractorContract.AlarmInteractor>(
            AlarmInteractor(mockk(), mockk(), mockk(), mockk(), mockk(), mockk()),
        )
    }

    private fun setupRepositoriesMock() {
        every {
            alarmRepositoryMock.getAlarm(any(), any())
        } returns
            CompletableDeferred(Result.success(fakeAlarm))

        every {
            settingsRepositoryMock.getSettings()
        } returns
            flowOf(Result.success(fakeSettings))
    }

    private val fakeAlarm =
        Alarm(
            id = 3L,
            Hour(3),
            Minute(30),
            WeeklyRepeat(),
            Label("3AM"),
            true,
            AlertType.SOUND_AND_VIBRATE,
            Ringtone("default_ringtone.mp3"),
            AlarmType.SAY_IT,
            SayItScripts(),
        )

    private val fakeSettings =
        Settings(
            TimeOut(180),
            Snooze(15),
            Theme.DARK,
        )
}
