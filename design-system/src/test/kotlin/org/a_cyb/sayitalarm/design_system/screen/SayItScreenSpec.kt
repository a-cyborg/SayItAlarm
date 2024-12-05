/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUIInfo
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.junit.Test
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class SayItScreenSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun whenVewModelStateIsInitial_displaysInitialScene() {
        val viewModel = SayItViewModelFake(Initial)

        subjectUnderTest.setContent {
            SayItScreen(viewModel = viewModel)
        }

        subjectUnderTest.onNodeWithText(getString(R.string.loading)).assertExists()
    }

    @Test
    fun whenViewModelStateIsListening_displaysInProgressListeningScene() {
        val viewModel = SayItViewModelFake(SayItUiState.Listening(sayItUiInfo))

        subjectUnderTest.setContent {
            SayItScreen(viewModel = viewModel)
        }

        subjectUnderTest.onNodeWithText(getString(R.string.listening)).assertExists()
        subjectUnderTest.onNodeWithText("${sayItUiInfo.currentCount} / ${sayItUiInfo.totalCount}").assertExists()
        subjectUnderTest.onNodeWithText(sayItUiInfo.script).assertExists()
        subjectUnderTest.onNodeWithText(sayItUiInfo.transcript).assertExists()
    }

    @Test
    fun whenViewModelStateIsSuccess_displaysInProgressSuccessScene() {
        val givenSayItInfo = sayItUiInfo.copy(transcript = sayItUiInfo.script)
        val viewModel = SayItViewModelFake(SayItUiState.Success(givenSayItInfo))

        subjectUnderTest.setContent {
            SayItScreen(viewModel = viewModel)
        }

        subjectUnderTest.onNodeWithText(getString(R.string.success)).assertExists()
        subjectUnderTest.onNodeWithText("${sayItUiInfo.currentCount} / ${sayItUiInfo.totalCount}").assertExists()
        assertEquals(
            2,
            subjectUnderTest.onAllNodesWithText(sayItUiInfo.script).fetchSemanticsNodes().size,
        )
    }

    @Test
    fun whenViewModelStateIsFailed_displaysInProgressFailedScene() {
        val viewModel = SayItViewModelFake(SayItUiState.Failed(sayItUiInfo))

        subjectUnderTest.setContent {
            SayItScreen(viewModel = viewModel)
        }

        subjectUnderTest.onNodeWithText(getString(R.string.failed)).assertExists()
        subjectUnderTest.onNodeWithText("${sayItUiInfo.currentCount} / ${sayItUiInfo.totalCount}").assertExists()
        subjectUnderTest.onNodeWithText(sayItUiInfo.script).assertExists()
        subjectUnderTest.onNodeWithText(sayItUiInfo.transcript).assertExists()
    }

    @Test
    fun whenViewModelStateIsError_displaysErrorScene() {
        val givenErrorMessage = "SERVICE_DISCONNECTED"
        val viewModel = SayItViewModelFake(SayItUiState.Error(givenErrorMessage))

        subjectUnderTest.setContent {
            SayItScreen(viewModel = viewModel)
        }

        subjectUnderTest.onNodeWithText(getString(R.string.error)).assertExists()
        subjectUnderTest.onNodeWithText(getString(R.string.info_say_it_error)).assertExists()
        subjectUnderTest
            .onNodeWithText("${getString(R.string.info_say_it_error_name)} $givenErrorMessage")
            .assertExists()
        subjectUnderTest.onNodeWithText(getString(R.string.exit))
            .assertExists()
            .assertHasClickAction()
            .performClick()
        assertEquals(SayItViewModelFake.InvokedType.FINISH, viewModel.invokedType)
    }

    @Test
    fun whenViewModelStateIsFinished_displaysFinishScene() {
        val viewModel = SayItViewModelFake(SayItUiState.Finished)

        subjectUnderTest.setContent {
            SayItScreen(viewModel = viewModel)
        }

        subjectUnderTest.onNodeWithText(getString(R.string.say_it)).assertExists()
        subjectUnderTest.onNodeWithText(getString(R.string.finish))
            .assertExists()
            .assertHasClickAction()
            .performClick()
        assertEquals(SayItViewModelFake.InvokedType.FINISH, viewModel.invokedType)
    }

    private val sayItUiInfo =
        SayItUIInfo(
            "I embrace this hour with enthusiasm.",
            "I embrace",
            1,
            3,
        )
}

private class SayItViewModelFake(state: SayItUiState) : SayItContract.SayItViewModel {
    override val state: StateFlow<SayItUiState> = MutableStateFlow(state)

    private var _invokedType: InvokedType = InvokedType.NONE
    val invokedType: InvokedType
        get() = _invokedType

    override fun processScript() {
        _invokedType = InvokedType.PROCESS_SCRIPT
    }

    override fun finish() {
        _invokedType = InvokedType.FINISH
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    enum class InvokedType {
        PROCESS_SCRIPT,
        FINISH,
        NONE,
    }
}
