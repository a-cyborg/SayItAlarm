/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
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
import org.a_cyb.sayitalarm.util.mustBe

@OptIn(ExperimentalCoroutinesApi::class)
class EditInteractorSpec {

    private val alarmRepository: RepositoryContract.AlarmRepository = mockk(relaxed = true)
    private val alarmScheduler: AlarmServiceContract.AlarmScheduler = mockk(relaxed = true)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun clear() {
        clearAllMocks()

        Dispatchers.resetMain()
    }

    @Test
    fun `When getAlarm is called it emits alarm`() = runTest {
        // Given
        every { alarmRepository.getAlarm(any(), any()) } returns
            async { Result.success(alarm) }

        val interactor = EditInteractor(alarmRepository, alarmScheduler)

        interactor.alarm.test {
            // When
            interactor.getAlarm(3, this)

            // Then
            awaitItem() mustBe Result.success(alarm)
        }

        coVerify(exactly = 1) { alarmRepository.getAlarm(any(), any()) }
    }

    @Test
    fun `When getAlarm is called it emits failure`() = runTest {
        // Given
        val result = Result.failure<Alarm>(IllegalStateException())

        every { alarmRepository.getAlarm(any(), any()) } returns async { result }

        val interactor = EditInteractor(alarmRepository, alarmScheduler)

        interactor.alarm.test {
            // When
            interactor.getAlarm(3, this)

            // Then
            awaitItem() mustBe result
        }

        coVerify(exactly = 1) { alarmRepository.getAlarm(any(), any()) }
    }

    @Test
    fun `When update is called it sequentially executes repository and scheduler coroutines`() = runTest {
        // Given
        val interactor = EditInteractor(alarmRepository, alarmScheduler)

        // When
        interactor.update(alarm, this)
        runCurrent()

        // Then
        coVerifyOrder {
            alarmRepository.update(any(), any())
            alarmScheduler.scheduleAlarms()
        }
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
