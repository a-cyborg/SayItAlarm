/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsInteractorSpec {

    private val settingsRepository: RepositoryContract.SettingsRepository = mockk(relaxed = true)

    private lateinit var interactor: SettingsInteractor

    private val settings = Settings(
        timeOut = TimeOut(180),
        snooze = Snooze(15),
        theme = Theme.LIGHT,
    )

    @BeforeTest
    fun setup() {
        interactor = SettingsInteractor((settingsRepository))
    }

    @AfterTest
    fun clear() {
        clearAllMocks()
    }

    @Test
    fun `When getSettings is called it returns flow`() = runTest {
        // Given
        val results = listOf(
            Result.failure(IllegalStateException()),
            Result.success(settings),
            Result.failure(IllegalStateException()),
        )

        every { settingsRepository.getSettings() } returns
            flow {
                results.forEach { emit(it) }
            }

        // When
        interactor.settings.test {
            // Then
            assertEquals(results[0], awaitItem())
            assertEquals(Result.success(settings), awaitItem())
            assertEquals(results[2], awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun `When insertOrIgnore is called it triggers repository insertOrIgnore`() = runTest {
        // Given
        val settings = Settings(TimeOut(180), Snooze(15), Theme.LIGHT)

        // When
        interactor.insertOrIgnore(settings, this)

        runCurrent()

        // Then
        verify(exactly = 1) {
            settingsRepository.insertOrIgnore(any(), any())
        }
    }

    @Test
    fun `When setTimeout is called it triggers repository setTimeOut`() = runTest {
        // Given
        val timeOut = TimeOut(300)

        // When
        interactor.setTimeOut(timeOut, this)

        runCurrent()

        // Then
        verify(exactly = 1) {
            settingsRepository.setTimeOut(timeOut, any())
        }
    }

    @Test
    fun `When setSnooze is called it triggers repository setSnooze`() = runTest {
        // Given
        val snooze = Snooze(33)

        // When
        interactor.setSnooze(snooze, this)

        runCurrent()

        // Then
        verify(exactly = 1) {
            settingsRepository.setSnooze(snooze, any())
        }
    }

    @Test
    fun `When setTheme is called it triggers repository setTheme`() = runTest {
        // Given
        val theme = Theme.DARK

        // When
        interactor.setTheme(theme, this)

        runCurrent()

        // Then
        verify(exactly = 1) {
            settingsRepository.setTheme(theme, any())
        }
    }

    @Test
    fun `It fulfills SettingsInteractor`() {
        assertIs<InteractorContract.SettingsInteractor>(interactor)
    }
}
