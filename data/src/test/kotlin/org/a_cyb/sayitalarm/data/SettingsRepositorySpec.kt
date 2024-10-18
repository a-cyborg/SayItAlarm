/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data

import app.cash.turbine.test
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.data.datasource.DataSourceContract
import org.a_cyb.sayitalarm.data.model.toSettings
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.a_cyb.sayitalarm.util.test_utils.fulfils
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import org.junit.Test
import kotlin.test.AfterTest
import org.acyb.sayitalarm.database.Get as SettingsDTO

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositorySpec {
    private val dataSource: DataSourceContract.SettingsDataSource = mockk(relaxed = true)

    @AfterTest
    fun clear() {
        clearMocks(dataSource)
    }

    @Test
    fun `When getSettings is starts to be collected it receives data from dataSource and send it downstream`() =
        runTest {
            // Given
            val results = listOf(
                Result.failure(NoSuchElementException()),
                Result.success(getSettingsDTO()),
                Result.failure(IllegalStateException()),
            )
            every { dataSource.getSettings(any()) } returns
                flow {
                    results.forEach { emit(it) }
                }

            val dispatcher = StandardTestDispatcher(this.testScheduler)
            val repository = SettingsRepository(dataSource, dispatcher)

            // When
            repository.getSettings().test {
                // Then
                awaitItem() mustBe results[0]
                awaitItem() mustBe Result.success(
                    Settings(
                        timeOut = TimeOut(300),
                        snooze = Snooze(15),
                        theme = Theme.LIGHT,
                    ),
                )
                awaitItem() mustBe results[2]

                awaitComplete()
            }
        }

    @Test
    fun `When insert is called it invoke dataSource insert`() = runTest {
        // Given
        val settingsToInsert = getSettingsDTO().toSettings()

        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = SettingsRepository(dataSource, dispatcher)

        // When
        repository.insertOrIgnore(settingsToInsert, this)

        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.insert(getSettingsDTO()) }
    }

    @Test
    fun `When setTimeOut is called it invoke dataSource setTimeOut`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = SettingsRepository(dataSource, dispatcher)

        // When
        repository.setTimeOut(TimeOut(150), this)

        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.setTimeOut(150) }
    }

    @Test
    fun `When setSnooze is called it invokes dataSource setSnooze`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = SettingsRepository(dataSource, dispatcher)

        // When
        repository.setSnooze(Snooze(80), this)

        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.setSnooze(80) }
    }

    @Test
    fun `When setTheme is called it invokes dataSource setTheme`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = SettingsRepository(dataSource, dispatcher)

        // When
        repository.setTheme(Theme.DARK, this)

        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.setTheme(1) }
    }

    @Test
    fun `It fulfills SettingsRepository`() {
        val dispatcher = StandardTestDispatcher()

        SettingsRepository(dataSource, dispatcher) fulfils
            RepositoryContract.SettingsRepository::class
    }

    private fun getSettingsDTO(): SettingsDTO =
        SettingsDTO(
            timeOut = 300,
            snooze = 15,
            theme = 0,
        )
}
