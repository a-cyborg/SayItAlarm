/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.domain.interactor

import kotlin.test.BeforeTest
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.util.fulfils
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test

class SettingsInteractorSpec {

    private val settingsRepository: RepositoryContract.SettingsRepository = mockk(relaxed = true)

    private lateinit var interactor: SettingsInteractor

    @BeforeTest
    fun setup() {
        interactor = SettingsInteractor((settingsRepository))
    }

    @Test
    fun `When load is called it propagates success with settings`() = runTest {
        // Given
        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.load(this)

            // Then
            awaitItem() mustBe Result.success(settings)
        }
    }

    @Test
    fun `When load is called it propagates failure with exception`() = runTest {
        // Given
        val exception = IllegalStateException()
        every { settingsRepository.load(any()) } returns async { Result.failure(exception) }

        interactor.settings.test {
            // When
            interactor.load(this)

            // Then
            awaitItem() mustBe Result.failure(exception)
        }

    }

    @Test
    fun `When load is called it triggers SettingsRepository load`() = runTest {
        // Given
        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.load(this)

            skipItems(1)
        }

        // Then
        verify(exactly = 1) {
            @Suppress("DeferredResultUnused")
            settingsRepository.load(any())
        }
    }

    @Test
    fun `When setTimeout is called it propagates success with settings`() = runTest {
        // Given
        val timeOut = TimeOut(300)
        val settings = settings.copy(timeOut = timeOut)

        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.setTimeOut(timeOut, this)

            // Then
            awaitItem() mustBe Result.success(settings)
        }
    }

    @Test
    fun `When setTimeout is called it triggers SettingsRepository setTimeOut and load`() = runTest {
        // Given
        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.setTimeOut(settings.timeOut, this)

            skipItems(1)
        }

        // Then
        verify(exactly = 1) { settingsRepository.setTimeOut(any(), any()) }
        verify(exactly = 1) {
            @Suppress("DeferredResultUnused")
            settingsRepository.load(any())
        }
    }

    @Test
    fun `When setSnooze is called it propagates success with settings`() = runTest {
        // Given
        val snooze = Snooze(33)
        val settings = settings.copy(snooze = snooze)

        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.setSnooze(snooze, this)

            // Then
            awaitItem() mustBe Result.success(settings)
        }
    }

    @Test
    fun `When setSnooze is called it triggers SettingsRepository setSnooze and load`() = runTest {
        // Given
        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.setSnooze(settings.snooze, this)

            skipItems(1)
        }

        // Then
        verify(exactly = 1) { settingsRepository.setSnooze(any(), any()) }
        verify(exactly = 1) {
            @Suppress("DeferredResultUnused")
            settingsRepository.load(any())
        }
    }

    @Test
    fun `When setTheme is called it triggers SettingsRepository setTheme and load`() = runTest {
        // Given
        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.setTheme(settings.theme, this)

            skipItems(1)
        }

        // Then
        verify(exactly = 1) { settingsRepository.setTheme(any(), any()) }
        verify(exactly = 1) {
            @Suppress("DeferredResultUnused")
            settingsRepository.load(any())
        }
    }

    @Test
    fun `When setTheme is called it propagates success with settings`() = runTest {
        // Given
        val theme = Theme.DARK
        val settings = settings.copy(theme = theme)

        every { settingsRepository.load(any()) } returns async { Result.success(settings) }

        interactor.settings.test {
            // When
            interactor.setTheme(theme, this)

            // Then
            awaitItem() mustBe Result.success(settings)
        }
    }

    @Test
    fun `It fulfills SettingsInteractor`() {
        interactor fulfils InteractorContract.SettingsInteractor::class
    }

    private val settings = Settings(
        timeOut = TimeOut(180),
        snooze = Snooze(15),
        theme = Theme.LIGHT,
    )
}
