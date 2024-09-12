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
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItState
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItState.Error
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItState.Finished
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItState.Processing
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SttStatus
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import org.robolectric.annotation.Config

@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class SayItScreenSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `When SayItViewModel is in the Initial state, It displays LoadingScreen`() {
        // Given
        val viewModel = SayItViewModelFake(Initial)

        subjectUnderTest.setContent {
            // When
            SayItScreen(viewModel = viewModel)
        }
        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.loading))
            .assertExists()
    }

    @Test
    fun `When SayItViewModel is in the Finished state, It displays FinishScreen`() {
        // Given
        val viewModel = SayItViewModelFake(Finished)

        subjectUnderTest.setContent {
            // When
            SayItScreen(viewModel = viewModel)
        }
        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.finish)).assertExists()
            .assertHasClickAction()
            .performClick()
        viewModel.invokedType mustBe SayItViewModelFake.InvokedType.FORCE_QUIT
    }

    @Test
    fun `When SayItViewModel is in the Error state, It displays ErrorScreen`() {
        // Given
        val viewModel = SayItViewModelFake(Error)

        subjectUnderTest.setContent {
            // When
            SayItScreen(viewModel = viewModel)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.error)).assertExists()
        subjectUnderTest.onNodeWithText(getString(R.string.info_say_it_error)).assertExists()
        subjectUnderTest.onNodeWithText(getString(R.string.exit)).assertExists()
            .assertHasClickAction()
            .performClick()
        viewModel.invokedType mustBe SayItViewModelFake.InvokedType.FORCE_QUIT
    }

    @Test
    fun `When SayItViewModel is in the Processing_Ready state, It displays ProcessingScreen`() {
        // Given
        val viewModel = SayItViewModelFake(Processing(sayItInfo))

        subjectUnderTest.setContent {
            // When
            SayItScreen(viewModel = viewModel)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.ready)).assertExists()
        subjectUnderTest.onNodeWithText("${sayItInfo.count.current}/${sayItInfo.count.total}").assertExists()
        subjectUnderTest.onNodeWithText(sayItInfo.script).assertExists()
        subjectUnderTest.onNodeWithText(getString(R.string.start)).assertExists()
            .assertHasClickAction()
            .performClick()
        viewModel.invokedType mustBe SayItViewModelFake.InvokedType.PROCESS_SCRIPT
    }

    @Test
    fun `When SayItViewModel is in the Processing_Listening state, It displays ProcessingScreen with Listening anim`() {
        // Given
        val sayItInfo = sayItInfo.copy(
            status = SttStatus.LISTENING,
            sttResult = "I emb",
        )
        val viewModel = SayItViewModelFake(Processing(sayItInfo))

        subjectUnderTest.setContent {
            // When
            SayItScreen(viewModel = viewModel)
        }

        // Then
        subjectUnderTest.onAllNodesWithText(getString(R.string.listening)).fetchSemanticsNodes()
            .size mustBe 2
        subjectUnderTest.onNodeWithText("${sayItInfo.count.current}/${sayItInfo.count.total}").assertExists()
        subjectUnderTest.onNodeWithText(sayItInfo.script).assertExists()
        subjectUnderTest.onNodeWithText("I emb").assertExists()
    }

    @Test
    fun `When SayItViewModel is in the Processing_Success state, It displays ProcessingScreen with Success action`() {
        // Given
        val sayItInfo = sayItInfo.copy(
            status = SttStatus.SUCCESS,
            sttResult = sayItInfo.script
        )
        val viewModel = SayItViewModelFake(Processing(sayItInfo))

        subjectUnderTest.setContent {
            // When
            SayItScreen(viewModel = viewModel)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.success)).assertExists()
        subjectUnderTest.onNodeWithText("${sayItInfo.count.current}/${sayItInfo.count.total}").assertExists()
        subjectUnderTest.onAllNodesWithText(sayItInfo.script).fetchSemanticsNodes()
            .size mustBe 2
        subjectUnderTest.onNodeWithText(getString(R.string.start)).assertExists()
            .assertHasClickAction()
            .performClick()
        viewModel.invokedType mustBe SayItViewModelFake.InvokedType.PROCESS_SCRIPT
    }

    @Test
    fun `When SayItViewModel is in the Processing_Failed state, It displays ProcessingScreen with Failed action`() {
        // Given
        val sayItInfo = sayItInfo.copy(
            status = SttStatus.FAILED,
            sttResult = "It's wrong"
        )
        val viewModel = SayItViewModelFake(Processing(sayItInfo))

        subjectUnderTest.setContent {
            // When
            SayItScreen(viewModel = viewModel)
        }

        // Then
        subjectUnderTest.onNodeWithText(getString(R.string.failed)).assertExists()
        subjectUnderTest.onNodeWithText("${sayItInfo.count.current}/${sayItInfo.count.total}").assertExists()
        subjectUnderTest.onNodeWithText(sayItInfo.script).assertExists()
        subjectUnderTest.onNodeWithText("It's wrong").assertExists()
        subjectUnderTest.onNodeWithText(getString(R.string.try_again)).assertExists()
            .assertHasClickAction()
            .performClick()
        viewModel.invokedType mustBe SayItViewModelFake.InvokedType.PROCESS_SCRIPT
    }

    private val sayItInfo = SayItContract.SayItInfo(
        script = "I embrace this hour with enthusiasm.",
        sttResult = "I embrace this",
        status = SttStatus.READY,
        count = SayItContract.Count(3, 7)
    )
}

private class SayItViewModelFake(state: SayItState) : SayItContract.SayItViewModel {
    override val state: StateFlow<SayItState> = MutableStateFlow(state)
    override val isOffline: StateFlow<SayItContract.IsOffline> = MutableStateFlow(SayItContract.IsOffline.True)

    private var _invokedType: InvokedType = InvokedType.NONE
    val invokedType: InvokedType
        get() = _invokedType

    override fun processScript() {
        _invokedType = InvokedType.PROCESS_SCRIPT
    }

    override fun finish() {
        _invokedType = InvokedType.FORCE_QUIT
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }

    enum class InvokedType {
        PROCESS_SCRIPT,
        FORCE_QUIT,
        NONE,
    }
}


