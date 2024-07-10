/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.FakeAlarmUIData
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.AddContract
import org.a_cyb.sayitalarm.presentation.AddContract.AddState
import org.a_cyb.sayitalarm.presentation.AddContract.AddState.Error
import org.a_cyb.sayitalarm.presentation.AddContract.AddState.Initial
import org.a_cyb.sayitalarm.presentation.AddContract.AddState.Success
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableRepeat
import org.a_cyb.sayitalarm.presentation.command.CommandContract.Command
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.ResizableExperimental)
class AddScreenSpec : RoborazziTest() {

    private val alarmUI = FakeAlarmUIData.defaultAlarmUI

    private fun stringRes(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `When AddViewModel is in Initial state it displays AlarmUI`() {
        // Given
        val state = Initial(alarmUI)
        val viewModel = AddViewModelFake(state)

        with(subjectUnderTest) {
            // When
            setContent {
                AddScreen(
                    viewModel = viewModel,
                    navigateToList = {},
                )
            }

            // Then
            onNodeWithText("8:00 AM")
                .assertExists()
        }
    }

    @Test
    fun `When AddViewModel is in Success state it displays AlarmUI`() {
        // Given
        val state = Success(alarmUI.copy(label = "Success"))
        val viewModel = AddViewModelFake(state)

        with(subjectUnderTest) {
            // When
            setContent {
                AddScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            // Then
            onNodeWithText("Success")
                .assertExists()
        }
    }

    @Test
    fun `When AddViewModel is in Error state it displays info text`() {
        // Given
        val state = Error(alarmUI)
        val viewModel = AddViewModelFake(state)

        with(subjectUnderTest) {
            // When
            setContent {
                AddScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            // Then
            onNodeWithText(stringRes(R.string.info_add_and_edit_initialize_error))
                .assertExists()
        }
    }

    @Test
    fun `When AddViewModel state is updated it displays new data`() {
        // Given
        val viewModel = AddViewModelFake(
            Initial(alarmUI),
            Success(alarmUI.copy(label = "Hi, there"))
        )

        with(subjectUnderTest) {
            setContent {
                AddScreen(
                    viewModel = viewModel,
                    navigateToList = {},
                )
            }

            // When
            onNode(hasSetTextAction())
                .performClick()
                .performImeAction() // It triggers SetLabelCommand execute.

            // Then
            onNodeWithText("Hi, there")
                .assertExists()
        }
    }

    @Test
    fun `When viewModel is in Success state and save is clicked it executes SaveCommand`() {
        // Given
        val viewModel = AddViewModelFake(Success(alarmUI))

        with(subjectUnderTest) {
            setContent {
                AddScreen(
                    viewModel = viewModel,
                    navigateToList = {}
                )
            }

            // When
            onNodeWithText(stringRes(R.string.save))
                .performClick()

            // Then
            viewModel.saveHasBeenCalled mustBe true
        }
    }
}

private class AddViewModelFake(vararg states: AddState) : AddContract.AddViewModel {

    private val states = states.toMutableList()

    private val _state: MutableStateFlow<AddState> = MutableStateFlow(Initial(FakeAlarmUIData.defaultAlarmUI))
    override val state: StateFlow<AddState> = _state

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

    override fun setTime(hour: Hour, minute: Minute) {}
    override fun setWeeklyRepeat(selectableRepeats: List<SelectableRepeat>) {}

    override fun setLabel(label: String) {
        updateState()
    }

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
