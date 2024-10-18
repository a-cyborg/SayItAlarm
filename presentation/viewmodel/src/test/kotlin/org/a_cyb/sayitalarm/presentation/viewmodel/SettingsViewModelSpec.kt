/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract.SettingsState.Error
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract.SettingsState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract.SettingsState.Success
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract.SettingsUI
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract.TimeInput
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.DurationFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.SettingsInteractorFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.SettingsInteractorFake.InvokedType
import org.a_cyb.sayitalarm.util.link_opener.LinkOpenerContract
import org.a_cyb.sayitalarm.util.test_utils.fulfils
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelSpec {

    private val settings = Settings(
        timeOut = TimeOut(180),
        snooze = Snooze(15),
        theme = Theme.LIGHT,
    )
    private val settingsUI = SettingsUI(
        timeOut = TimeInput(settings.timeOut.timeOut, "3 hr"),
        snooze = TimeInput(settings.snooze.snooze, "15 min"),
        theme = "Light",
    )

    private val interactor = SettingsInteractorFake(listOf(Result.failure(IllegalStateException())))
    private val durationFormatter = DurationFormatterFake()
    private val linkOpener: LinkOpenerContract = mockk(relaxed = true)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It is in the initial state`() {
        SettingsViewModel(interactor, durationFormatter, linkOpener).state.value mustBe Initial
    }

    @Test
    fun `Given interactor fails it sets InitialError state`() = runTest {
        val result = listOf(Result.failure<Settings>(RuntimeException()))
        val interactor = SettingsInteractorFake(result)
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)

        viewModel.state.test {
            // When
            skipItems(1)

            // Then
            awaitItem() mustBe Error
        }
    }

    @Test
    fun `Given interactor success result with Settings it is in success state`() = runTest {
        // Given
        val results = listOf(Result.success(settings))
        val interactor = SettingsInteractorFake(results)
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)

        viewModel.state.test {
            // When
            skipItems(1) // Initial state

            // Then
            awaitItem() mustBe Success(settingsUI)
        }
    }

    @Test
    fun `When it initialized it triggers repository insertOrIgnore`() = runTest {
        // Given
        val results = listOf(Result.success(settings))
        val interactor = SettingsInteractorFake(results)

        // When
        SettingsViewModel(interactor, durationFormatter, linkOpener)

        // Then
        interactor.invoked mustBe InvokedType.INSERT_OR_IGNORE
    }

    @Test
    fun `Given setTimeOut is called it propagates Success`() = runTest {
        // Given
        val timeOut = TimeOut(60)
        val settingsUI = settingsUI.copy(
            timeOut = TimeInput(timeOut.timeOut, "1 hr"),
        )

        val results = listOf(
            Result.success(settings),
            Result.success(settings.copy(timeOut = timeOut)),
        )
        val interactor = SettingsInteractorFake(results)
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)

        viewModel.state.test {
            skipItems(2)

            // When
            viewModel.setTimeOut(timeOut)

            // Then
            awaitItem() mustBe Success(settingsUI)
            interactor.invoked mustBe InvokedType.SET_TIMEOUT
        }
    }

    @Test
    fun `Given setSnooze is called it propagates Success`() = runTest {
        // Given
        val snooze = Snooze(20)
        val settingsUI = settingsUI.copy(
            snooze = TimeInput(snooze.snooze, "20 min"),
        )

        val results = listOf(
            Result.success(settings),
            Result.success(settings.copy(snooze = snooze)),
        )
        val interactor = SettingsInteractorFake(results)
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)

        viewModel.state.test {
            skipItems(2)
            advanceUntilIdle()

            // When
            viewModel.setSnooze(snooze)

            // Then
            awaitItem() mustBe Success(settingsUI)

            interactor.invoked mustBe InvokedType.SET_SNOOZE
        }
    }

    @Test
    fun `Given setTheme is called it propagates Success`() = runTest {
        // Given
        val theme = Theme.DARK
        val settingsUI = settingsUI.copy(theme = "Dark")

        val results = listOf(
            Result.success(settings),
            Result.success(settings.copy(theme = theme)),
        )
        val interactor = SettingsInteractorFake(results)
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)

        viewModel.state.test {
            skipItems(2)
            advanceUntilIdle()

            // When
            viewModel.setTheme("Dark")

            // Then
            awaitItem() mustBe Success(settingsUI)
            interactor.invoked mustBe InvokedType.SET_THEME
        }
    }

    @Test
    fun `When sendEmail is called it triggers LinkOpener openEmail`() {
        // Given
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)
        val emailSlot = slot<String>()
        val titleSlot = slot<String>()

        // When
        viewModel.sendEmail()

        // Then
        verify { linkOpener.openEmail(capture(emailSlot), capture(titleSlot)) }
        emailSlot.captured mustBe "SayItAlarm@gmail.com"
        titleSlot.captured mustBe ""
    }

    @Test
    fun `When openGooglePlay is called it triggers LinkOpener openGooglePlay`() {
        // Given
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)

        // When
        viewModel.openGooglePlay()

        // Then
        verify { linkOpener.openGooglePlay() }
    }

    @Test
    fun `When openGitHub is called it triggers LinkOpener openBrowserLink`() {
        // Given
        val viewModel = SettingsViewModel(interactor, durationFormatter, linkOpener)
        val linkSlot = slot<String>()

        // When
        viewModel.openGitHub()

        // Then
        verify { linkOpener.openBrowserLink(capture(linkSlot)) }
        linkSlot.captured mustBe "https://github.com/a-cyborg/SayItAlarm"
    }

    @Test
    fun `Given runCommand is called it executes the given command`() {
        // Given
        val command: CommandContract.Command<SettingsViewModel> = mockk(relaxed = true)

        // When
        SettingsViewModel(interactor, durationFormatter, linkOpener).runCommand(command)

        // Then
        verify(exactly = 1) { command.execute(any()) }
    }

    @Test
    fun `It fulfils SettingsViewModel`() {
        SettingsViewModel(interactor, durationFormatter, linkOpener) fulfils
            SettingsContract.SettingsViewModel::class
    }
}
