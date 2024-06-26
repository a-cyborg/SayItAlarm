/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.SettingsContract
import org.a_cyb.sayitalarm.presentation.SettingsContract.*
import org.a_cyb.sayitalarm.presentation.SettingsContract.SettingsState.*
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.DurationFormatterFake
import org.a_cyb.sayitalarm.presentation.viewmodel.fake.SettingsInteractorFake
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

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

    private val interactor = SettingsInteractorFake(listOf(Result.failure(IllegalStateException())), TestScope())
    private val durationFormatter = DurationFormatterFake()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It fulfils SettingsViewModel`() {
        SettingsViewModel(interactor, durationFormatter) fulfils
            SettingsContract.SettingsViewModel::class
    }

    @Test
    fun `It is in the initial state`() {
        SettingsViewModel(interactor, durationFormatter)
            .state.value mustBe Initial
    }

    @Test
    fun `Given interactor fails it sets InitialError state`() = runTest {
        val result = listOf(Result.failure<Settings>(RuntimeException()))
        val interactor = SettingsInteractorFake(result, this)
        val viewModel = SettingsViewModel(interactor, durationFormatter)

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
        val interactor = SettingsInteractorFake(results, this)
        val viewModel = SettingsViewModel(interactor, durationFormatter)

        viewModel.state.test {
            // When
            skipItems(1) // Initial state

            // Then
            awaitItem() mustBe Success(settingsUI)
        }
    }

    @Test
    fun `Given setTimeOut is called it propagates SettingsStateWithContent`() = runTest {
        // Given
        val timeOut = TimeOut(60)
        val settingsUI = settingsUI.copy(
            timeOut = TimeInput(timeOut.timeOut, "1 hr"),
        )

        val results = listOf(
            Result.success(settings),
            Result.success(settings.copy(timeOut = timeOut)),
        )
        val interactor = SettingsInteractorFake(results, this)
        val viewModel = SettingsViewModel(interactor, durationFormatter)

        viewModel.state.test {
            skipItems(2)

            // When
            viewModel.setTimeOut(timeOut)

            // Then
            awaitItem() mustBe Success(settingsUI)
            interactor.invoked mustBe SettingsInteractorFake.InvokedType.SET_TIMEOUT
        }
    }

    @Test
    fun `Given setSnooze is called it propagates SettingsStateWithContent`() = runTest {
        // Given
        val snooze = Snooze(20)
        val settingsUI = settingsUI.copy(
            snooze = TimeInput(snooze.snooze, "20 min"),
        )

        val results = listOf(
            Result.success(settings),
            Result.success(settings.copy(snooze = snooze)),
        )
        val interactor = SettingsInteractorFake(results, this)
        val viewModel = SettingsViewModel(interactor, durationFormatter)

        viewModel.state.test {
            skipItems(2)
            advanceUntilIdle()

            // When
            viewModel.setSnooze(snooze)

            // Then
            awaitItem() mustBe Success(settingsUI)

            interactor.invoked mustBe SettingsInteractorFake.InvokedType.SET_SNOOZE
        }
    }

    @Test
    fun `Given setTheme is called it propagates SettingsStateWithContent`() = runTest {
        // Given
        val theme = Theme.DARK
        val settingsUI = settingsUI.copy(theme = "Dark")

        val results = listOf(
            Result.success(settings),
            Result.success(settings.copy(theme = theme)),
        )
        val interactor = SettingsInteractorFake(results, this)
        val viewModel = SettingsViewModel(interactor, durationFormatter)

        viewModel.state.test {
            skipItems(2)
            advanceUntilIdle()

            // When
            viewModel.setTheme("Dark")

            // Then
            awaitItem() mustBe Success(settingsUI)
            interactor.invoked mustBe SettingsInteractorFake.InvokedType.SET_THEME
        }
    }

    @Test
    fun `Given runCommand is called it executes the given command`() {
        // Given
        val command: CommandContract.Command<SettingsViewModel> = mockk(relaxed = true)

        // When
        SettingsViewModel(interactor, durationFormatter).runCommand(command)

        // Then
        verify(exactly = 1) { command.execute(any()) }
    }
}
