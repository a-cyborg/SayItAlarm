/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.ui.test.onNodeWithText
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    fun `When AlarmViewModel is in the Initial state, it displays the RingingScreen`() {
        // Given
        val viewModel = AlarmViewModelFake()

        with(subjectUnderTest) {
            // When
            setContent {
                AlarmScreen(viewModel = viewModel)
            }

            // Then
            onNodeWithText("8:00 AM").assertExists()
            onNodeWithText(getString(R.string.say_it)).assertExists()
            onNodeWithText(getString(R.string.snooze)).assertExists()
        }
    }
}

private class AlarmViewModelFake() : AlarmContract.AlarmViewModel {

    private val _state: MutableStateFlow<AlarmUiState> = MutableStateFlow(AlarmUiState.Initial)
    override val state: StateFlow<AlarmUiState> = _state.asStateFlow()

    override val currentTime: StateFlow<String> = MutableStateFlow("8:00 AM")

    override fun startSayIt() {
        _state.value = AlarmUiState.VoiceInputProcessing
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}