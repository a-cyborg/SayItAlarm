/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import java.util.Calendar
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.presentation.CommandContract
import org.a_cyb.sayitalarm.presentation.add.AddContract
import org.a_cyb.sayitalarm.presentation.interactor.AddInteractorContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.EnumFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

class AddViewModelSpec {
    private val interactor = AddInteractorFake()
    private val weekdayFormatter = WeekdayFormatterFake()
    private val enumFormatter = EnumFormatterFake()

    private val alarmUi = AddContract.AlarmUi(
        hour = 8,
        minute = 0,
        weeklyRepeat = weekdayFormatter.formatAbbr(emptySet()),
        label = "",
        alertType = enumFormatter.formatAlertType(AlertType.SOUND_AND_VIBRATE),
        ringtone = "",
        sayItScripts = emptyList()
    )

    private val alarm = Alarm(
        id = 1,
        hour = Hour(6),
        minute = Minute(0),
        weeklyRepeat = WeeklyRepeat(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY),
        label = Label("Wake Up"),
        enabled = true,
        alertType = AlertType.SOUND_ONLY,
        ringtone = Ringtone("file://wake_up_alarm.mp3"),
        alarmType = AlarmType.SAY_IT,
        sayItScripts = SayItScripts("I am peaceful and whole.")
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It fulfills AddViewModel`() {
        AddViewModel(interactor, weekdayFormatter, enumFormatter) fulfils AddContract.AddViewModel::class
    }

    @Test
    fun `It is in Initial state`() {
        AddViewModel(interactor, weekdayFormatter, enumFormatter).state.value mustBe AddContract.Initial
    }

    @Test
    fun `It sets AddStateWithContent`() = runTest {
        // Given
        val viewModel = AddViewModel(interactor, weekdayFormatter, enumFormatter)

        viewModel.state.test {
            // When
            skipItems(1)

            // Then
            awaitItem() mustBe AddContract.AddStateWithContent(alarmUi)
        }
    }

    @Test
    fun `Given save is called it calls interactor save`() = runTest {
        // Given
        val interactor: AddInteractorContract = mockk(relaxed = true)
        val viewModel = AddViewModel(interactor, weekdayFormatter, enumFormatter)

        // When
        viewModel.save(alarm)

        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { interactor.save(any()) }
    }

    @Test
    fun `Given runCommand is called it executes the given command`() {
        // Given
        val command: CommandContract.Command<AddViewModel> = mockk(relaxed = true)

        // When
        AddViewModel(interactor, weekdayFormatter, enumFormatter).runCommand(command)

        // Then
        verify(exactly = 1) { command.execute(any()) }
    }
}

private class AddInteractorFake : AddInteractorContract {
    override suspend fun save(alarm: Alarm) {
    }
}