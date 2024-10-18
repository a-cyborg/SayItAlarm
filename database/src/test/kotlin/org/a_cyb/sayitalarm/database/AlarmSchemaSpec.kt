/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import org.acyb.sayitalarm.database.Alarm
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull

class AlarmSchemaSpec {

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

    @Test
    fun `When insert query is executed it stores an alarm`() {
        // Given
        val alarmEntity = getRandomAlarmEntity()

        // When
        sayItDB.alarmQueries.insert(
            alarmEntity.hour,
            alarmEntity.minute,
            alarmEntity.weeklyRepeat,
            alarmEntity.label,
            alarmEntity.enabled,
            alarmEntity.alertType,
            alarmEntity.ringtone,
            alarmEntity.alarmType,
            alarmEntity.sayItScripts,
        )

        val alarms = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // Then
        alarms.isNotEmpty() mustBe true
    }

    @Test
    fun `When update query is executed it updates given id of alarm`() {
        // Given
        val alarmEntity = getRandomAlarmEntity(hour = 11, minute = 11)

        insertAlarm(listOf(alarmEntity))

        val fetched = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()
            .first()

        fetched.hour mustBe 11
        fetched.minute mustBe 11

        // When
        sayItDB.alarmQueries.update(
            3,
            3,
            fetched.weeklyRepeat,
            fetched.label,
            fetched.enabled,
            fetched.alertType,
            fetched.ringtone,
            fetched.alarmType,
            fetched.sayItScripts,
            fetched.id,
        )

        val actual = sayItDB.alarmQueries
            .getById(fetched.id)
            .executeAsOne()

        // Then
        actual.hour mustBe 3
        actual.minute mustBe 3
        actual.label mustBe alarmEntity.label
    }

    @Test
    fun `When updateEnabled query is executed it updates enabled`() {
        // Given
        val alarmEntity = getRandomAlarmEntity(enabled = false)

        insertAlarm(listOf(alarmEntity))

        val fetched = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()
            .first()

        fetched.enabled mustBe false

        // When
        sayItDB.alarmQueries
            .updateEnabled(true, fetched.id)

        val updated = sayItDB.alarmQueries
            .getById(fetched.id)
            .executeAsOne()

        // Then
        updated.enabled mustBe true
    }

    @Test
    fun `When getAllByTimeAsc query is executed it fetches all stored alarmsEntities ordered by ascending time`() {
        // Given
        val alarmEntities = listOf(
            getRandomAlarmEntity(hour = 6, minute = 0),
            getRandomAlarmEntity(hour = 6, minute = 33),
            getRandomAlarmEntity(hour = 12, minute = 22),
            getRandomAlarmEntity(hour = 20, minute = 0),
            getRandomAlarmEntity(hour = 20, minute = 33),
        )

        insertAlarm(alarmEntities)

        // When
        val fetched = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // Then
        fetched.forEachIndexed { index, actual ->
            val expected = alarmEntities[index]

            actual.hour mustBe expected.hour
            actual.minute mustBe expected.minute
        }
    }

    @Test
    fun `When getAllByTimeAsc query is executed it returns emptyList`() {
        // When
        val fetched = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // Then
        fetched mustBe emptyList()
    }

    @Test
    fun `When getById query is executed it returns alarmEntity of given Id `() {
        // Given
        val alarmEntities = List(5) { getRandomAlarmEntity() }

        insertAlarm(alarmEntities)

        val fetchId: Long = fixture.fixture(range = 1..5)

        // When
        val actual = sayItDB.alarmQueries
            .getById(fetchId)
            .executeAsOne()

        // Then
        val expected = alarmEntities[(fetchId - 1).toInt()]

        actual.hour mustBe expected.hour
        actual.minute mustBe expected.minute
        actual.weeklyRepeat mustBe expected.weeklyRepeat
        actual.enabled mustBe expected.enabled
        actual.alertType mustBe expected.alertType
        actual.ringtone mustBe expected.ringtone
        actual.alarmType mustBe expected.alarmType
        actual.sayItScripts mustBe expected.sayItScripts
    }

    @Test
    fun `When getById query is executed it returns null`() {
        assertNull(
            sayItDB.alarmQueries
                .getById(5)
                .executeAsOneOrNull(),
        )
    }

    @Test
    fun `When getAllEnabledAlarm is executed it returns enabled alarms`() {
        // Given
        val alarmEntities = List(5) { idx ->
            getRandomAlarmEntity(enabled = idx % 2 == 0)
        }

        insertAlarm(alarmEntities)

        // When
        val actual = sayItDB.alarmQueries
            .getAllEnabledAlarm()
            .executeAsList()

        val expected = alarmEntities
            .mapIndexed { idx, alarm -> alarm.copy(id = (idx + 1).toLong()) }
            .filterIndexed { idx, _ -> idx % 2 == 0 }

        // Then
        actual mustBe expected
    }

    @Test
    fun `When getAllEnabledAlarm is called it returns empty list`() {
        val alarmEntities = List(5) { getRandomAlarmEntity(enabled = false) }

        insertAlarm(alarmEntities)

        // When
        val expected = sayItDB.alarmQueries
            .getAllEnabledAlarm()
            .executeAsList()

        // Then
        expected mustBe emptyList()
    }

    @Test
    fun `When delete query is executed it deletes row`() {
        // Given
        val alarmEntities = List(5) { getRandomAlarmEntity() }

        insertAlarm(alarmEntities)

        val idToDelete: Long = fixture.fixture(range = 1..6)

        // When
        sayItDB.alarmQueries
            .delete(idToDelete)

        val actual = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // Then
        actual.isEmpty() mustBe false
        actual.firstOrNull { it.id == idToDelete } mustBe null
    }

    @Test
    fun `When delete query is executed with non existent id nothing changes`() {
        // Given
        val alarmEntities = List(5) { getRandomAlarmEntity() }

        insertAlarm(alarmEntities)

        val initialEntities = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // When
        sayItDB.alarmQueries
            .delete(8)

        val entitiesAfterDelete = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // Then
        initialEntities mustBe entitiesAfterDelete
    }

    private fun insertAlarm(alarms: List<Alarm>) {
        alarms.forEach {
            sayItDB.alarmQueries
                .insert(
                    it.hour,
                    it.minute,
                    it.weeklyRepeat,
                    it.label,
                    it.enabled,
                    it.alertType,
                    it.ringtone,
                    it.alarmType,
                    it.sayItScripts,
                )
        }
    }

    private fun getRandomAlarmEntity(
        hour: Long = fixture.fixture(range = 0..23),
        minute: Long = fixture.fixture(range = 0..59),
        weeklyRepeat: Long = fixture.fixture(),
        label: String = fixture.fixture(),
        enabled: Boolean = fixture.fixture(),
        alertType: Long = fixture.fixture(),
        ringtone: String = fixture.fixture(),
        alarmType: Long = fixture.fixture(),
        sayItScripts: String = fixture.fixture(),
    ): Alarm =
        Alarm(
            id = 0L,
            hour,
            minute,
            weeklyRepeat,
            label,
            enabled,
            alertType,
            ringtone,
            alarmType,
            sayItScripts,
        )
}
