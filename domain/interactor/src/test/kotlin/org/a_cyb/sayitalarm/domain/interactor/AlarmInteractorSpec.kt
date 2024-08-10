/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
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
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmInteractorSpec {

    private val alarmRepository: RepositoryContract.AlarmRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun clear() {
        clearAllMocks()

        Dispatchers.resetMain()
    }

    @Test
    fun `When getAlarm is called and alarmRepository returns an alarm, It emits a Result success`() = runBlocking {
        // Given
        every { alarmRepository.getAlarm(any(), any()) } returns
            async { Result.success(alarm) }

        val interactor = AlarmInteractor(alarmRepository, UnconfinedTestDispatcher())

        // When
        val actual = interactor.getAlarm(0, this)

        // Then
        actual mustBe Result.success(alarm)
    }

    @Test
    fun `When getAlarm is called and alarmRepository failed to fetch an alarm, It emits a Result failure`() =
        runBlocking {
            // Given
            every { alarmRepository.getAlarm(any(), any()) } returns
                async { Result.failure(IllegalStateException("Alarm not found.")) }

            val interactor = AlarmInteractor(alarmRepository, UnconfinedTestDispatcher())

            // When
            val actual = interactor.getAlarm(0, this)

            // Then
            actual.isFailure mustBe true
            (actual.exceptionOrNull() is IllegalStateException) mustBe true
            (actual.exceptionOrNull()?.message) mustBe "Alarm not found."
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