/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.presentation.contracts.ListContract
import org.a_cyb.sayitalarm.presentation.contracts.ListContract.ListState
import org.a_cyb.sayitalarm.presentation.contracts.ListContract.ListState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.ListContract.ListState.InitialError
import org.a_cyb.sayitalarm.presentation.contracts.ListContract.ListState.Success
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import org.robolectric.annotation.Config

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class ListScreenSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `When ListViewModel is in success state with alarm data it displays them`() {
        // Given
        val states = listOf(Success(alarmData))
        val viewModel = ListViewModelFake(states)

        with(subjectUnderTest) {
            // When
            setContent {
                ListScreen(
                    viewModel = viewModel,
                    navigateToSettings = {},
                    navigateToAdd = {},
                    navigateToEdit = {},
                )
            }

            // Then
            onNodeWithText("6:00 AM")
                .assertExists()
        }
    }

    @Test
    fun `When ListViewModel with success state without alarm data it displays info text`() = runTest {
        // Given
        val states = listOf(Success(emptyList()))
        val viewModel = ListViewModelFake(states)

        with(subjectUnderTest) {
            // When
            setContent {
                ListScreen(
                    viewModel = viewModel,
                    navigateToSettings = {},
                    navigateToAdd = {},
                    navigateToEdit = {},
                )
            }

            // Then
            onNodeWithText(getString(R.string.info_list_no_alarm))
                .assertExists()
        }
    }

    @Test
    fun `When ListViewModel is in InitialError state it displays error text`() = runTest {
        // Given
        val states = listOf(InitialError)
        val viewModel = ListViewModelFake(states)

        with(subjectUnderTest) {
            // When
            setContent {
                ListScreen(
                    viewModel = viewModel,
                    navigateToSettings = {},
                    navigateToAdd = {},
                    navigateToEdit = {},
                )
            }

            // Then
            onNodeWithText(getString(R.string.info_list_initialize_error))
                .assertExists()
        }
    }

    @Test
    fun `When edit button is clicked it goes into edit mode`() = runTest {
        // Given
        val states = listOf(Success(alarmData))
        val viewModel = ListViewModelFake(states)

        with(subjectUnderTest) {
            setContent {
                ListScreen(
                    viewModel = viewModel,
                    navigateToSettings = {},
                    navigateToAdd = {},
                    navigateToEdit = {},
                )
            }

            // When
            onNodeWithText(getString(R.string.edit))
                .performClick()

            // Then
            onNodeWithText(getString(R.string.done))
                .assertExists()
        }
    }

    @Test
    fun `When in edit mode and done button is clicked return to view mode`() = runTest {
        // Given
        val states = listOf(Success(alarmData))
        val viewModel = ListViewModelFake(states)

        with((subjectUnderTest)) {
            setContent {
                ListScreen(
                    viewModel = viewModel,
                    navigateToSettings = {},
                    navigateToAdd = {},
                    navigateToEdit = {},
                )
            }

            onNodeWithText(getString(R.string.edit))
                .performClick()

            // When
            onNodeWithText(getString(R.string.done))
                .performClick()

            // Then
            onNodeWithText(getString(R.string.edit))
                .assertExists()
        }
    }

    @Test
    fun `When switch is clicked it executes SetEnabledCommand`() = runTest {
        // Given
        val states = listOf(Success(alarmData), Success(alarmData))
        val viewModel = ListViewModelFake(states)

        with(subjectUnderTest) {
            setContent {
                ListScreen(
                    viewModel = viewModel,
                    navigateToSettings = {},
                    navigateToAdd = {},
                    navigateToEdit = {},
                )
            }

            // When
            onAllNodes(isToggleable()).onLast()
                .performClick()

            // Then
            viewModel.invokedType mustBe ListViewModelFake.InvokedType.SET_ENABLED
        }
    }

    @Test
    fun `When delete button is clicked it executes DeleteAlarmCommand`() = runTest {
        // Given
        val states = listOf(
            Success(alarmData),
            Success(alarmData.toMutableList().dropLast(1))
        )
        val viewModel = ListViewModelFake(states)

        with(subjectUnderTest) {
            setContent {
                ListScreen(
                    viewModel = viewModel,
                    navigateToSettings = {},
                    navigateToAdd = {},
                    navigateToEdit = {},
                )
            }

            onNodeWithText(getString(R.string.edit))
                .performClick()

            // When
            onAllNodesWithContentDescription(getString(R.string.action_delete_alarm)).onLast()
                .performClick()

            // Then
            viewModel.invokedType mustBe ListViewModelFake.InvokedType.DELETE_ALARM
            onNodeWithText(alarmData.last().time)
                .assertDoesNotExist()
        }
    }

    private val alarmData = listOf(
        ListContract.AlarmInfo(
            id = 1,
            time = "6:00 AM",
            labelAndWeeklyRepeat = "Wake Up, every weekday",
            enabled = true
        ),
        ListContract.AlarmInfo(
            id = 2,
            time = "8:30 PM",
            labelAndWeeklyRepeat = "Workout, Mon, Wed, and Fri",
            enabled = true
        ),
        ListContract.AlarmInfo(
            id = 3,
            time = "9:00 AM",
            labelAndWeeklyRepeat = "Passion Hour, every weekend",
            enabled = false
        ),
    )
}

private class ListViewModelFake(
    states: List<ListState>,
) : ListContract.ListViewModel {
    private val states = states.toMutableList()

    private val _state: MutableStateFlow<ListState> = MutableStateFlow(Initial)
    override val state: StateFlow<ListState> = _state
    override val isOfflineAvailable: StateFlow<Boolean> = MutableStateFlow(true)

    private var _invokedType: InvokedType = InvokedType.NONE
    val invokedType: InvokedType
        get() = _invokedType

    init {
        load()
    }

    private fun updateState() {
        _state.update {
            states.removeFirst()
        }
    }

    private fun load() {
        updateState()
    }

    override fun setEnabled(id: Long, enabled: Boolean) {
        _invokedType = InvokedType.SET_ENABLED

        updateState()
    }

    override fun deleteAlarm(id: Long) {
        _invokedType = InvokedType.DELETE_ALARM

        updateState()
    }

    override fun downloadRecognizerModel() {}

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    enum class InvokedType {
        SET_ENABLED,
        DELETE_ALARM,
        NONE
    }
}
