/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.database.SayItDB
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.acyb.sayitalarm.database.Alarm as AlarmDTO

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
    fun `When insert is called it stores an alarm`() = runTest {
        // Given
        val alarmToInsert = getRandomAlarmDto()

        // When
        dataSource.insert(alarmToInsert)

        val actual = sayItDB.alarmQueries
            .getById(1)
            .executeAsOne()

        // Then
        actual mustBe alarmToInsert.copy(id = 1)
    }

    @Test
    fun `When getAllByTimeAsc is called it returns flow of stored alarms`() = runTest {
        // Given
        val alarmsToInsert = listOf(
            getRandomAlarmDto(hour = 23, minute = 20),
            getRandomAlarmDto(hour = 22, minute = 0),
            getRandomAlarmDto(hour = 18, minute = 30),
            getRandomAlarmDto(hour = 18, minute = 0),
            getRandomAlarmDto(hour = 8, minute = 10),
            getRandomAlarmDto(hour = 8, minute = 5),
        )
        val dispatcher = StandardTestDispatcher(this.testScheduler)

        alarmsToInsert.forEach {
            dataSource.insert(it)
        }

        // When
        val actual = dataSource.getAllByTimeAsc(dispatcher).first()

        // Then
        val expected = alarmsToInsert
            .mapIndexed { idx, alarm -> alarm.copy(id = (idx + 1).toLong()) }
            .sortedWith(compareBy(AlarmDTO::hour, AlarmDTO::minute))

        actual mustBe Result.success(expected)
    }

    @Test
    fun `When getById is called with invalid id it returns failure result`() = runTest {
        // When
        val result = dataSource.getById(8L)

        // Then
        result.isFailure mustBe true
    }

    @Test
    fun `When getById is called it returns alarmDTO`() = runTest {
        // Given
        val alarmsToInsert = List(3) { getRandomAlarmDto() }

        alarmsToInsert.forEach {
            dataSource.insert(it)
        }

        val idToGet: Long = fixture.fixture(range = 1..3)

        // When
        val actual = dataSource.getById(idToGet)

        val expected = alarmsToInsert[idToGet.toInt() - 1]
            .copy(id = idToGet)

        // Then
        actual mustBe Result.success(expected)
    }

    @Test
    fun `When getAllEnabled is called it returns enabled alarms`() = runTest {
        // Given
        val alarmsToInsert = List(5) { idx ->
            getRandomAlarmDto(enabled = idx % 2 == 0)
        }

        alarmsToInsert.forEach {
            dataSource.insert(it)
        }

        // When
        val actual = dataSource.getAllEnabled()

        val expected = alarmsToInsert
            .mapIndexed { idx, alarm -> alarm.copy(id = (idx + 1).toLong()) }
            .filterIndexed { idx, _ -> idx % 2 == 0 }

        // Then
        actual mustBe expected
    }

    @Test
    fun `When getAllEnabled is called it returns empty list`() = runTest {
        // Given
        val alarmsToInsert = List(5) { getRandomAlarmDto(enabled = false) }

        alarmsToInsert.forEach {
            dataSource.insert(it)
        }

        // When
        val actual = dataSource.getAllEnabled()

        // Then
        actual mustBe emptyList()
    }

    @Test
    fun `When update is called it updates stored alarm`() = runTest {
        // Given
        val alarm = getRandomAlarmDto(hour = 11, minute = 11)

        dataSource.insert(alarm)

        val expected = alarm
            .copy(id = 1L, hour = 3, minute = 3)

        dataSource
            .getAllByTimeAsc(dispatcher = StandardTestDispatcher(this.testScheduler))
            .test {
                skipItems(1)

                // When
                dataSource.update(expected)

                val actual = awaitItem().getOrNull()!!.first()

                // Then
                actual.hour mustBe 3
                actual.minute mustBe 3
                actual.weeklyRepeat mustBe alarm.weeklyRepeat
            }
    }

    @Test
    fun `When updateEnabled is called it sets enabled on alarm of given id`() = runTest {
        // Given
        val alarmsToInsert = List(3) { getRandomAlarmDto(enabled = false) }

        alarmsToInsert.forEach {
            dataSource.insert(it)
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
    fun `When delete is called it deletes the alarm`() = runTest {
        // Given
        val alarmsToInsert = List(3) { getRandomAlarmDto() }

        alarmsToInsert.forEach {
            dataSource.insert(it)
        }

        val idToDelete: Long = fixture.fixture(range = 1..3)

        // When
        dataSource.delete(idToDelete)

        val actual = sayItDB.alarmQueries
            .getAllByTimeAsc()
            .executeAsList()

        // Then
        actual.size mustBe 2
        actual.find { it.id == idToDelete } mustBe null
    }

    private fun getRandomAlarmDto(
        hour: Long = fixture.fixture(range = 0..23),
        minute: Long = fixture.fixture(range = 0..59),
        weeklyRepeat: Long = fixture.fixture(),
        label: String = fixture.fixture(),
        enabled: Boolean = fixture.fixture(),
        alertType: Long = fixture.fixture(),
        ringtone: String = fixture.fixture(),
        alarmType: Long = fixture.fixture(),
        sayItScripts: String = fixture.fixture(),
    ): AlarmDTO =
        AlarmDTO(
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
