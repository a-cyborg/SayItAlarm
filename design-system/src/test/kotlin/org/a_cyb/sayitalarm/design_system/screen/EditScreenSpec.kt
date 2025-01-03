/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.design_system.FakeAlarmUIData
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel.EditState
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel.EditState.Error
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel.EditState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.EditContract.EditViewModel.EditState.Success
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import org.junit.Test
import org.robolectric.annotation.Config

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class EditScreenSpec : RoborazziTest() {

    private val alarmUI = FakeAlarmUIData.defaultAlarmUI

    private fun stringRes(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `When EditViewModel is in Success state it displays AlarmUI`() {
        // Given
        val alarmUI = alarmUI.copy(
            timeUI = TimeUI(3, 33, "3: 33 AM"),
        )
        val viewModel = EditViewModelFake(Success(alarmUI))

        with(subjectUnderTest) {
            // When
            setContent {
                EditScreen(
                    viewModel = viewModel,
                    navigateToList = {},
                )
            }

            // Then
            onNodeWithText("3: 33 AM")
                .assertExists()
        }
    }

    @Test
    fun `When EditViewModel is in Error state it displays error message`() {
        // Given
        val viewModel = EditViewModelFake(Error)

        with(subjectUnderTest) {
            // When
            setContent {
                EditScreen(
                    viewModel = viewModel,
                    navigateToList = {},
                )
            }

            // Then
            onNodeWithText(stringRes(R.string.info_add_and_edit_initialize_error))
                .assertExists()
        }
    }

    @Test
    fun `When EditViewModel success state is updated it displays updated data`() {
        // Given
        val viewModel = EditViewModelFake(
            Success(alarmUI),
            Success(alarmUI.copy(timeUI = TimeUI(6, 0, "6:00 AM"))),
        )

        with(subjectUnderTest) {
            setContent {
                EditScreen(
                    viewModel = viewModel,
                    navigateToList = {},
                )
            }

            onNodeWithText("8:00 AM")
                .performClick()

            // When
            onAllNodesWithText(stringRes(R.string.confirm)).onLast()
                .performClick()

            // Then
            onNodeWithText("6:00 AM")
                .assertExists()
        }
    }

    @Test
    fun `When EditViewModel is in Success state and confirm is clicked it executes SaveCommand`() {
        // Given
        val viewModel = EditViewModelFake(Success(alarmUI))

        with(subjectUnderTest) {
            setContent {
                EditScreen(
                    viewModel = viewModel,
                    navigateToList = {},
                )
            }

            // When
            onNodeWithText(stringRes(R.string.confirm))
                .performClick()

            // Then
            viewModel.saveHasBeenCalled mustBe true
        }
    }
}

private class EditViewModelFake(vararg states: EditState) : EditViewModel {

    private val states = states.toMutableList()

    private val _state: MutableStateFlow<EditState> = MutableStateFlow(Initial)
    override val state: StateFlow<EditState> = _state

    private var _saveHasBeenCalled: Boolean = false
    val saveHasBeenCalled: Boolean
        get() = _saveHasBeenCalled

    init {
        updateState()
    }

    private fun updateState() {
        _state.update {
            states.removeFirst()
        }
    }

    override fun setTime(hour: Hour, minute: Minute) {
        updateState()
    }

    override fun setWeeklyRepeat(selectableRepeats: List<SelectableRepeat>) {}
    override fun setLabel(label: String) {}
    override fun setAlertType(alertTypeName: String) {}
    override fun setRingtone(ringtoneUI: RingtoneUI) {}
    override fun setScripts(scripts: SayItScripts) {}
    override fun save() {
        _saveHasBeenCalled = true
    }

    override fun <T : CommandReceiver> runCommand(command: Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
