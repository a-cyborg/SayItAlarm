/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.alarm_service.core.AlarmServiceContract.ServiceEvent
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract.AlarmControllerState
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Ringtone
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmControllerSpec {
    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var alarmController: AlarmController

    @Before
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()
        alarmController = AlarmController(testDispatcher)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When connectService is called, it updates the state to ServiceConnected`() = runTest {
        // Given
        val givenAlarmId = 1L
        val alarmServiceMock: AlarmServiceContract = mockk(relaxed = true)

        alarmController.alarmControllerState.test {
            skipItems(1) // Initial

            // When
            alarmController.connectService(alarmServiceMock, givenAlarmId)

            // Then
            assertEquals(AlarmControllerState.ServiceConnected(givenAlarmId), awaitItem())
        }
    }

    @Test
    fun `When connectService is called, it sets the alarmService`() {
        // Given
        val alarmServiceMock: AlarmServiceContract = mockk(relaxed = true)
        val givenRingtone = Ringtone("rainbow.mp3")
        val givenAlertType = AlertType.SOUND_AND_VIBRATE

        // When
        alarmController.connectService(alarmServiceMock, 0L)

        alarmController.startAlarm(givenRingtone, givenAlertType)
        alarmController.stopAlarm()
        alarmController.stopService()

        // Then
        verify { alarmServiceMock.startRinging(givenRingtone, givenAlertType) }
        verify { alarmServiceMock.stopRinging() }
        verify { alarmServiceMock.stopService() }
    }

    @Test
    fun `When connectService is called, it begins collecting the AlarmServiceEvent`() = runTest {
        // Given
        val alarmServiceMock: AlarmServiceContract = mockk(relaxed = true)
        val alarmServiceEventMock = MutableSharedFlow<ServiceEvent>()

        every { alarmServiceMock.serviceEvent } returns alarmServiceEventMock

        assertEquals(0, alarmServiceEventMock.subscriptionCount.value)

        // When
        alarmController.connectService(alarmServiceMock, 0L)

        // Then
        assertEquals(1, alarmServiceEventMock.subscriptionCount.value)
    }

    @Test
    fun `When ServiceEvent AudioVibPlayerError occurs, it updates the state to AudioPlayerError`() =
        runTest {
            // Given
            val givenExceptionMessage = "audioPlayerIsNotInitialized."
            val alarmServiceMock: AlarmServiceContract = mockk(relaxed = true)
            val alarmServiceEventMockk = MutableSharedFlow<ServiceEvent>()

            every { alarmServiceMock.serviceEvent } returns alarmServiceEventMockk

            alarmController.alarmControllerState.test {
                skipItems(1)
                alarmController.connectService(alarmServiceMock, 0L)
                skipItems(1)

                // When
                alarmServiceEventMockk.emit(
                    ServiceEvent.AudioVibePlayerError(
                        IllegalStateException(givenExceptionMessage),
                    ),
                )

                // Then
                assertEquals(
                    AlarmControllerState.AudioPlayerError(givenExceptionMessage),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `When serviceDisconnected is called, it update the state to ServiceDisconnected`() = runTest {
        alarmController.alarmControllerState.test {
            skipItems(1)

            // When
            alarmController.serviceDisconnected()

            // Then
            assertEquals(
                AlarmControllerState.ServiceDisconnected,
                awaitItem(),
            )
        }
    }

    @Test
    fun `When service is disconnected and startAlarm is called, it updates the state to ServiceDisconnected`() =
        runTest {
            alarmController.alarmControllerState.test {
                skipItems(1) // Initial

                // When
                alarmController.startAlarm(Ringtone(""), AlertType.SOUND_AND_VIBRATE)

                // Then
                assertEquals(
                    AlarmControllerState.ServiceDisconnected,
                    awaitItem(),
                )
            }
        }

    @Test
    fun `It fulfills AlarmControllerContract`() {
        assertIs<AlarmControllerContract>(AlarmController(testDispatcher))
    }
}
