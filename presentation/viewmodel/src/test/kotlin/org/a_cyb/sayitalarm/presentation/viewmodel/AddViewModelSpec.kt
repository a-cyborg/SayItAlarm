/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.presentation.add.AddContract
import org.a_cyb.sayitalarm.presentation.add.AddContract.AddStateWithContent
import org.a_cyb.sayitalarm.presentation.add.AddContract.Initial
import org.a_cyb.sayitalarm.presentation.alarm_panel.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.alarm_panel.SetTimeCommand
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.AlarmPanelInteractorFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.EnumFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.RingtoneManagerFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.WeekdayFormatterFake
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelSpec {

    private val defaultAlarmUI = AlarmPanelContract.AlarmUI(
        hour = 8,
        minute = 0,
        weeklyRepeat = "",
        label = "",
        alertType = "Sound and vibration",
        ringtone = "Radial",
        sayItScripts = emptyList()
    )

    private fun getAlarmPanelViewModel( id: Long = 0L, scope: CoroutineScope = TestScope()) =
        AlarmPanelViewModel(
            id,
            scope,
            AlarmPanelInteractorFake(),
            RingtoneManagerFake(),
            WeekdayFormatterFake(),
            EnumFormatterFake(),
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
        val viewModel = AddViewModel(AddInteractorFake(), getAlarmPanelViewModel(scope = TestScope()))

        viewModel fulfils AddContract.AddViewModel::class
    }

    @Test
    fun `It is in Initial state`() {
        val viewModel = AddViewModel(AddInteractorFake(), getAlarmPanelViewModel(scope = TestScope()))

        viewModel.state.value mustBe Initial
    }

    @Test
    fun `It sets AddStateWithContent`() = runTest {
        // Given
        val viewModel = AddViewModel(AddInteractorFake(), getAlarmPanelViewModel())

        viewModel.state.test {
            // When
            skipItems(1)

            // Then
            awaitItem() mustBe AddStateWithContent(defaultAlarmUI)
        }
    }

    @Test
    fun `Given alarmPanelCommand is executed it propagates AddStateWithContent`() = runTest {
        // Given
        val viewModel = AddViewModel(AddInteractorFake(), getAlarmPanelViewModel())
        val command = SetTimeCommand(hour = Hour(3), minute = Minute(33))

        // When
        viewModel.alarmPanelExecutor(command)

        viewModel.state.test {
            skipItems(2) // Initial & AddStateWithContent with default alarmUI

            // Then
            awaitItem() mustBe AddStateWithContent(defaultAlarmUI.copy(hour = 3, minute = 33))
        }
    }

    @Test
    fun `Given SaveCommand is executed, it calls AddInteractor's save method`() = runTest {
        // Given
        val interactor: InteractorContract.AddInteractor = mockk(relaxed = true)
        val viewModel = AddViewModel(interactor, getAlarmPanelViewModel())

        // When
        viewModel.save()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { interactor.save(any(), any()) }
    }
}

private class AddInteractorFake : InteractorContract.AddInteractor {
    override suspend fun save(alarm: Alarm, scope: CoroutineScope) {}
}
