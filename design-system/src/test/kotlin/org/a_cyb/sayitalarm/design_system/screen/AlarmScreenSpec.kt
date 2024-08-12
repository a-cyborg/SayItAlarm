/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.onNodeWithText
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.presentation.AlarmContract
import org.a_cyb.sayitalarm.presentation.AlarmContract.AlarmUiState
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.junit.Test
import org.robolectric.annotation.Config

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class AlarmScreenSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `When AlarmViewModel is in the Initial state, it displays the loading circle`() {
        // Given
        val viewModel = AlarmViewModelFake()

        with(subjectUnderTest) {
            // When
            setContent {
                AlarmScreen(viewModel = viewModel)
            }

            // Then
            onNodeWithText(getString(R.string.say_it)).assertExists()
            onNodeWithText(getString(R.string.loading)).assertExists()
        }
    }

    @Test
    fun `When AlarmViewModel is in the Ringing state, it displays the Ringing screen`() {
        // Given
        val label = "Good morning🌻"
        val viewModel = AlarmViewModelFake(AlarmUiState.Ringing(label))

        with(subjectUnderTest) {
            // When
            setContent {
                AlarmScreen(viewModel = viewModel)
            }

            // Then
            onNodeWithText(viewModel.currentTime.value).assertExists()
            onNodeWithText(label).assertExists()
            onNodeWithText(getString(R.string.say_it))
                .assertExists()
                .assertHasClickAction()
            onNodeWithText(getString(R.string.snooze))
                .assertExists()
                .assertHasClickAction()
        }
    }
}

private class AlarmViewModelFake(state: AlarmUiState = AlarmUiState.Initial) : AlarmContract.AlarmViewModel {
    override val state: StateFlow<AlarmUiState> = MutableStateFlow(state)
    override val currentTime: StateFlow<String> = MutableStateFlow("8:00 AM")

    override fun startSayIt() {}
    override fun finishAlarm() {}
    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {}
}