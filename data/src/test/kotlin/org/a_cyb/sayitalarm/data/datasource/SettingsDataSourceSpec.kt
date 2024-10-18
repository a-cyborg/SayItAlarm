/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.turbine.test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.database.SayItDB
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import org.acyb.sayitalarm.database.Get as SettingsDTO

class SettingsDataSourceSpec {
    private lateinit var driver: SqlDriver
    private lateinit var sayItDB: SayItDB
    private lateinit var dataSource: SettingsDataSource

    private val fixture = kotlinFixture()

    @BeforeTest
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        SayItDB.Schema.create(driver)

        sayItDB = SayItDB(driver)
        dataSource = SettingsDataSource(sayItDB.settingsQueries)
    }

    @AfterTest
    fun cleanUp() {
        driver.close()
    }

    @Test
    fun `When getSettings is called and data does not exist it returns failure`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)

        dataSource
            // WHen
            .getSettings(dispatcher)
            .test {
                // Then
                assertTrue { awaitItem().isFailure }
            }
    }

    @Test
    fun `When insert is called getSettings emits a success with settings DTO`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)

        dataSource
            .getSettings(dispatcher)
            .test {
                skipItems(1)

                // When
                dataSource.insert(getSettingsDTO())

                // Then
                awaitItem() mustBe Result.success(getSettingsDTO())
            }
    }

    @Test
    fun `When setTimeOut is called it updates settings timeOut`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val timeOutToSet: Long = fixture.fixture(range = 30..300)

        dataSource
            .getSettings(dispatcher)
            .test {
                dataSource.insert(getSettingsDTO())

                skipItems(2)

                // When
                dataSource.setTimeOut(timeOutToSet)

                // Then
                awaitItem()
                    .getOrNull()!!
                    .timeOut mustBe timeOutToSet
            }
    }

    @Test
    fun `When setSnooze is called it updates settings snooze`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val snoozeToSet: Long = fixture.fixture(range = 5..60)

        dataSource
            .getSettings(dispatcher)
            .test {
                dataSource.insert(getSettingsDTO())

                skipItems(2)

                // When
                dataSource.setSnooze(snoozeToSet)

                // Then
                awaitItem()
                    .getOrNull()!!
                    .snooze mustBe snoozeToSet
            }
    }

    @Test
    fun `When setTheme is called it updates settings theme`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val themeToSet = Theme.DARK.ordinal.toLong()

        dataSource
            .getSettings(dispatcher)
            .test {
                dataSource.insert(getSettingsDTO())

                skipItems(2)

                // When
                dataSource.setTheme(themeToSet)

                // Then
                awaitItem()
                    .getOrNull()!!
                    .theme mustBe themeToSet
            }
    }

    private fun getSettingsDTO() =
        SettingsDTO(
            timeOut = 180,
            snooze = 15,
            theme = 0,
        )
}
