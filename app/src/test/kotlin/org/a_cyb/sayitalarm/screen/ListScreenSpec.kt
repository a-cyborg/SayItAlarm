/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.screen

import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.ListContract
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = RobolectricDeviceQualifiers.SmallPhone)
class ListScreenSpec : RoborazziTest() {

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
    private val success = ListContract.Success(alarmData)

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `Given ListViewModel with success state it displays alarms`() = runTest {
        // Given
        val viewModel = ListViewModelFake(this, listOf(success))

        // When
        subjectUnderTest.setContent {
            ListScreen(viewModel = viewModel)
        }
        advanceUntilIdle()

        // Then
        subjectUnderTest.onNodeWithText("6:00 AM").assertExists()
    }

    @Test
    fun `Given ListViewModel with success state it displays info text`() = runTest {
        // Given
        val viewModel = ListViewModelFake(this, listOf(ListContract.Success(listOf())))

        // When
        subjectUnderTest.setContent {
            ListScreen(viewModel = viewModel)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(org.a_cyb.sayitalarm.R.string.info_list_no_alarm))
    }

    @Test
    fun `Given ListViewModel with InitialError state it displays info text`() = runTest {
        // Given
        val viewModel = ListViewModelFake(this, listOf(ListContract.InitialError))

        // When
        subjectUnderTest.setContent {
            ListScreen(viewModel = viewModel)
        }
        advanceUntilIdle()

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.info_list_initialize_error)).assertExists()
    }

    @Test
    fun `Given edit button is clicked it goes into edit mode`() = runTest {
        // Given
        val viewModel = ListViewModelFake(this, listOf(success))

        subjectUnderTest.setContent {
            ListScreen(viewModel = viewModel)
        }
        advanceUntilIdle()

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.edit)).performClick()

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.done)).assertExists()
    }

    @Test
    fun `Given it in an edit mode and done button is clicked it goes back to view mode`() = runTest {
        // Given
        val viewModel = ListViewModelFake(this, listOf(success))

        subjectUnderTest.setContent {
            ListScreen(viewModel = viewModel)
        }

        advanceUntilIdle()

        subjectUnderTest.onNodeWithText(getString(R.string.edit)).performClick()

        // When
        subjectUnderTest.onNodeWithText(getString(R.string.done)).performClick()

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.edit)).assertExists()
    }

    @Test
    fun `Given switch checked is changed it executes SetEnabledCommand`() = runTest {
        // When
        val updatedAlamData = alarmData
            .toMutableList().apply { set(2, alarmData[2].copy(enabled = true)) }
            .toList()
        val viewModel = ListViewModelFake(
            this,
            listOf(success, ListContract.Success(updatedAlamData))
        )

        subjectUnderTest.setContent {
            ListScreen(viewModel = viewModel)
            advanceUntilIdle()
        }

        // When
        subjectUnderTest.onAllNodes(isToggleable()).onLast().performClick()
        advanceUntilIdle()

        // Then
        subjectUnderTest.onAllNodes(isToggleable()).onLast().assertIsOn()
    }

    @Test
    fun `Given delete button is clicked it executes DeleteAlarmCommand`() = runTest {
        // Given
        val updatedAlamData = alarmData.toMutableList().dropLast(1)
        val viewModel = ListViewModelFake(
            this,
            listOf(success, ListContract.Success(updatedAlamData))
        )

        subjectUnderTest.setContent {
            ListScreen(viewModel = viewModel)
        }
        advanceUntilIdle()

        subjectUnderTest.onNodeWithText(getString(R.string.edit)).performClick()

        // When
        subjectUnderTest.onAllNodesWithContentDescription(getString(R.string.action_delete_alarm)).onLast()
            .performClick()
        advanceUntilIdle()

        // Then
        subjectUnderTest.onNodeWithText(alarmData.last().time).assertDoesNotExist()
    }
}

private class ListViewModelFake(
    private val viewModelScope: CoroutineScope,
    states: List<ListContract.ListState>,
) : ListContract.ListViewModel {
    private val states = states.toMutableList()

    private val _state: MutableStateFlow<ListContract.ListState> = MutableStateFlow(ListContract.Initial)
    override val state: StateFlow<ListContract.ListState> = _state

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.emit(states.removeFirst())
        }
    }

    override fun setEnabled(id: Long, enabled: Boolean) {
        viewModelScope.launch {
            _state.emit(states.removeFirst())
        }
    }

    override fun deleteAlarm(id: Long) {
        viewModelScope.launch {
            _state.emit(states.removeFirst())
        }
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}