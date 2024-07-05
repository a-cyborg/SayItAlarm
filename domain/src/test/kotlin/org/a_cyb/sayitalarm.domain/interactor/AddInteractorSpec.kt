/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.util.fulfils
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddInteractorSpec {
    private val alarmRepository: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler = mockk(relaxed = true)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun clear() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `When save is called it triggers AlarmRepository save`() = runTest {
        // Given
        val interactor = AddInteractor(alarmRepository, alarmScheduler)

        // When
        interactor.save(alarm, this)
        runCurrent()

        // Then
        verify(exactly = 1) {
            alarmRepository.save(any(), any())
        }
    }

    @Test
    fun `When save is called it triggers AlarmScheduler setAlarm`() = runTest {
        // Given
        val interactor = AddInteractor(alarmRepository, alarmScheduler)

        // When
        interactor.save(alarm, this)
        runCurrent()

        // Then
        coVerify(exactly = 1) {
            alarmScheduler.setAlarm(any())
        }
    }

    @Test
    fun `When save is called it runs repository save and scheduler setAlarm in order`() = runTest {
        // Given
        val interactor = AddInteractor(alarmRepository, alarmScheduler)

        // When
        interactor.save(alarm, this)
        advanceUntilIdle()

        coVerifyOrder {
            alarmRepository.save(any(), any())
            alarmScheduler.setAlarm(any())
        }
    }

    @Test
    fun `It fulfills AddInteractor`() {
        AddInteractor(
            alarmRepository,
            alarmScheduler
        ) fulfils InteractorContract.AddInteractor::class
    }

    private val alarm =
        Alarm(
            id = 1,
            hour = Hour(6),
            minute = Minute(0),
            weeklyRepeat = WeeklyRepeat(),
            label = Label(""),
            enabled = true,
            alertType = AlertType.SOUND_ONLY,
            ringtone = Ringtone(""),
            alarmType = AlarmType.SAY_IT,
            sayItScripts = SayItScripts()
        )
}
