/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.a_cyb.sayitalarm.util.mustBe
import org.acyb.sayitalarm.database.Settings
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

class SettingsSchemaSpec {

    private lateinit var driver: SqlDriver
    private lateinit var sayItDB: SayItDB

    private val fixture = kotlinFixture()

    @BeforeTest
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        SayItDB.Schema.create(driver)

        sayItDB = SayItDB(driver)
    }

    @AfterTest
    fun cleanUp() {
        driver.close()
    }

    private fun getDefaultSettings() =
        Settings(
            true,
            180,
            15,
            0
        )

    @Test
    fun `When insert query is executed it stores a Settings data with Id 1`() {
        // Given
        val settingsEntity = getDefaultSettings()

        // When
        sayItDB.settingsQueries
            .insert(
                settingsEntity.timeOut,
                settingsEntity.snooze,
                settingsEntity.theme
            )

        val fetched = sayItDB.settingsQueries
            .get()
            .executeAsOneOrNull()

        // Then
        assertNotNull(fetched)
        fetched.timeOut mustBe settingsEntity.timeOut
        fetched.snooze mustBe settingsEntity.snooze
        fetched.theme mustBe settingsEntity.theme
    }

    @Test
    fun `When updateTimeOut query is executed it updates timeOut`() {
        // Given
        val settingsEntity = getDefaultSettings()
        val timeOutToUpdate: Long = fixture.fixture()

        sayItDB.settingsQueries
            .insert(
                settingsEntity.timeOut,
                settingsEntity.snooze,
                settingsEntity.theme
            )

        // When
        sayItDB.settingsQueries
            .updateTimeOut(timeOutToUpdate)

        val fetched = sayItDB.settingsQueries
            .get()
            .executeAsOne()

        fetched.timeOut mustBe timeOutToUpdate
    }

    @Test
    fun `When updateSnooze query is executed it updates snooze`() {
        // Given
        val settingsEntity = getDefaultSettings()
        val snoozeToUpdate: Long = fixture.fixture()

        sayItDB.settingsQueries
            .insert(
                settingsEntity.timeOut,
                settingsEntity.snooze,
                settingsEntity.theme
            )

        // When
        sayItDB.settingsQueries
            .updateSnooze(snoozeToUpdate)

        val fetched = sayItDB.settingsQueries
            .get()
            .executeAsOne()

        fetched.snooze mustBe snoozeToUpdate
    }

    @Test
    fun `When updateTheme query is executed it updates snooze`() {
        // Given
        val settingsEntity = getDefaultSettings()
        val themeToUpdate: Long = fixture.fixture()

        sayItDB.settingsQueries
            .insert(
                settingsEntity.timeOut,
                settingsEntity.snooze,
                settingsEntity.theme
            )

        // When
        sayItDB.settingsQueries
            .updateTheme(themeToUpdate)

        val fetched = sayItDB.settingsQueries
            .get()
            .executeAsOne()

        fetched.theme mustBe themeToUpdate
    }
}
