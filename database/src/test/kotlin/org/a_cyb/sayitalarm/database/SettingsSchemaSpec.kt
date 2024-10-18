/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.a_cyb.sayitalarm.util.test_utils.isNot
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import org.acyb.sayitalarm.database.Settings
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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
            0,
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
                settingsEntity.theme,
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
    fun `When row is already exists it ignores insert call`() {
        // Given
        val existingSettings = getDefaultSettings()
            .copy(
                timeOut = 300,
                snooze = 30,
                theme = 1,
            )

        sayItDB.settingsQueries
            .insert(
                existingSettings.timeOut,
                existingSettings.snooze,
                existingSettings.theme,
            )

        val newInsertSettings = getDefaultSettings()

        // When
        sayItDB.settingsQueries
            .insert(
                newInsertSettings.timeOut,
                newInsertSettings.snooze,
                newInsertSettings.theme,
            )

        val actual = sayItDB.settingsQueries
            .get()
            .executeAsOne()

        // Then
        actual.timeOut mustBe existingSettings.timeOut
        actual.snooze mustBe existingSettings.snooze
        actual.theme mustBe existingSettings.theme

        actual.timeOut isNot newInsertSettings.timeOut
        actual.snooze isNot newInsertSettings.snooze
        actual.theme isNot newInsertSettings.theme
    }

    @Test
    fun `When get query is called it returns null`() {
        // When
        val settingsEntity = sayItDB.settingsQueries
            .get()
            .executeAsOneOrNull()

        // Then
        assertNull(settingsEntity)
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
                settingsEntity.theme,
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
                settingsEntity.theme,
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
                settingsEntity.theme,
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
