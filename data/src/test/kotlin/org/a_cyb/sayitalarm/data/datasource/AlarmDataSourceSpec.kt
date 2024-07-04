/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.data.model.AlarmEntity
import org.a_cyb.sayitalarm.database.SayItDB
import org.a_cyb.sayitalarm.util.mustBe
import org.acyb.sayitalarm.database.Alarm
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

class AlarmDataSourceSpec {

    private lateinit var driver: SqlDriver
    private lateinit var sayItDB: SayItDB
    private lateinit var dataSource: AlarmDataSource

    private val fixture = kotlinFixture()

    @BeforeTest
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        SayItDB.Schema.create(driver)

        sayItDB = SayItDB(driver)
        dataSource = AlarmDataSource(sayItDB.alarmQueries)
    }

    @AfterTest
    fun cleanUp() {
        driver.close()
    }

    @Test
    fun `When getAllByTimeAsc is called it returns flow of success result with stored alarms`() = runTest {
        // Given
        val alarms = List(3) { getRandomAlarm() }
        val dispatcher = StandardTestDispatcher(this.testScheduler)

        alarms.forEach {
            sayItDB.alarmQueries.insert(
                it.hour,
                it.minute,
                it.weeklyRepeat,
                it.label,
                it.enabled,
                it.alertType,
                it.ringtone,
                it.alarmType,
                it.sayItScripts
            )
        }

        // When
        val item = dataSource.getAllByTimeAsc(dispatcher).first()

        // Then
        val alarmEntities = alarms.mapIndexed { index, alarm ->
            alarm.copy(id = (index + 1).toLong())
                .toAlarmEntity()
        }

        item mustBe Result.success(alarmEntities)
    }

    // @Test
    // fun `When getById is called it returns failure result`() = runTest {
    //     // When
    //     val result = dataSource.getById(8L)
    //
    //     // Then
    //     result mustBe Result.failure(IllegalStateException())
    // }

    @Test
    fun `When getById is called it returns success result with alarmEntity`() = runTest {
        // Given
        val alarms = List(3) { getRandomAlarm() }

        alarms.forEach {
            sayItDB.alarmQueries.insert(
                it.hour,
                it.minute,
                it.weeklyRepeat,
                it.label,
                it.enabled,
                it.alertType,
                it.ringtone,
                it.alarmType,
                it.sayItScripts
            )
        }

        val idToGet: Long = fixture.fixture(range = 1..3)

        // When
        val result = dataSource.getById(idToGet)

        val expected = alarms[idToGet.toInt() - 1]
            .copy(id = idToGet)
            .toAlarmEntity()

        // Then
        result mustBe Result.success(expected)
    }

    @Test
    fun `When insert is called it stores an alarm`() = runTest {
        // Given
        val alarm = getRandomAlarm()

        // When
        dataSource.insert(alarm.toAlarmEntity())

        val actual = sayItDB.alarmQueries
            .getById(1)
            .executeAsOne()

        // Then
        actual mustBe alarm.copy(id = 1)
    }

    @Test
    fun `When update is called it updated stored alarm`() = runTest {
        // Given
        val alarm = getRandomAlarm(hour = 11, minute = 11)

        sayItDB.alarmQueries.insert(
            alarm.hour,
            alarm.minute,
            alarm.weeklyRepeat,
            alarm.label,
            alarm.enabled,
            alarm.alertType,
            alarm.ringtone,
            alarm.alarmType,
            alarm.sayItScripts
        )

        val updated = alarm
            .copy(id = 1L, hour = 3, minute = 3)
            .toAlarmEntity()

        // When
        dataSource.update(updated)

        val actual = sayItDB.alarmQueries
            .getById(1)
            .executeAsOne()

        // Then
        actual.hour mustBe 3
        actual.minute mustBe 3
        actual.weeklyRepeat mustBe alarm.weeklyRepeat
    }

    @Test
    fun `When updateEnabled is called it sets enabled on alarm of given id`() = runTest {
        // Given
        val alarms = List(3) { getRandomAlarm(enabled = false) }

        alarms.forEach {
            sayItDB.alarmQueries.insert(
                it.hour,
                it.minute,
                it.weeklyRepeat,
                it.label,
                it.enabled,
                it.alertType,
                it.ringtone,
                it.alarmType,
                it.sayItScripts
            )
        }

        val idToUpdate: Long = fixture.fixture(range = 1..3)

        // When
        dataSource.updateEnabled(idToUpdate, true)

        val actual = sayItDB.alarmQueries
            .getById(idToUpdate)
            .executeAsOne()

        // Then
        actual.enabled mustBe true
    }

    @Test
    fun `When delete is called it delete the alarm`() = runTest {
        // Given
        val alarms = List(3) { getRandomAlarm() }

        alarms.forEach {
            sayItDB.alarmQueries.insert(
                it.hour,
                it.minute,
                it.weeklyRepeat,
                it.label,
                it.enabled,
                it.alertType,
                it.ringtone,
                it.alarmType,
                it.sayItScripts
            )
        }

        val idToDelete: Long = fixture.fixture(range = 1..3)

        // When
        dataSource.delete(idToDelete)

        val allAlarms = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // Then
        allAlarms.size mustBe 2
        allAlarms.find { it.id == idToDelete } mustBe null
    }

    private fun getRandomAlarm(
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
            sayItScripts
        )

    private fun Alarm.toAlarmEntity(): AlarmEntity =
        AlarmEntity(
            id = this.id,
            hour = this.hour,
            minute = this.minute,
            weeklyRepeat = this.weeklyRepeat,
            label = this.label,
            enabled = this.enabled,
            alertType = this.alertType,
            ringtone = this.ringtone,
            alarmType = this.alarmType,
            sayItScripts = this.sayItScripts,
        )
}
