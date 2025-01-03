/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package org.a_cyb.sayitalarm.data

import app.cash.turbine.test
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.data.datasource.DataSourceContract
import org.a_cyb.sayitalarm.data.model.toAlarm
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.entity.AlarmType
import org.a_cyb.sayitalarm.entity.AlertType
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Label
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.Ringtone
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.entity.WeeklyRepeat
import org.a_cyb.sayitalarm.util.test_utils.fulfils
import org.a_cyb.sayitalarm.util.test_utils.mustBe
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import kotlin.test.AfterTest
import kotlin.test.Test
import org.acyb.sayitalarm.database.Alarm as AlarmDTO

class AlarmRepositorySpec {
    private val dataSource: DataSourceContract.AlarmDataSource = mockk(relaxed = true)

    private val fixture = kotlinFixture()

    @AfterTest
    fun clear() {
        clearMocks(dataSource)
    }

    @Test
    fun `When getAllAlarm starts to be collected it receives data from dataSource and send it downstream`() = runTest {
        // Given
        val results = listOf(
            Result.failure(IllegalStateException()),
            Result.success(listOf(getRandomDTO())),
            Result.failure(IllegalStateException()),
        )
        val dispatcher = StandardTestDispatcher()
        val repository = AlarmRepository(dataSource, dispatcher)

        every { dataSource.getAllByTimeAsc(dispatcher) } returns
            flow {
                results.forEach { emit(it) }
            }

        // When
        repository.getAllAlarms().test {
            // Then
            awaitItem().isFailure mustBe true
            awaitItem().isSuccess mustBe true
            awaitItem().isFailure mustBe true

            awaitComplete()
        }
    }

    @Test
    fun `GetAllAlarm receives data from dataSource and perform mapping before sending it downstream`() = runTest {
        // Given
        val dto = getRandomDTO(
            // 0b1111111 : Everyday
            weeklyRepeat = 127,
            alertType = AlertType.SOUND_AND_VIBRATE.ordinal.toLong(),
            alarmType = AlarmType.SAY_IT.ordinal.toLong(),
            sayItScripts = "First script,Second script,Third script",
        )
        val dispatcher = StandardTestDispatcher()
        val repository = AlarmRepository(dataSource, dispatcher)

        every { dataSource.getAllByTimeAsc(dispatcher) } returns
            flow {
                emit(Result.success(listOf(dto)))
            }

        // When
        repository.getAllAlarms().test {
            // Then
            awaitItem() mustBe Result.success(
                listOf(
                    Alarm(
                        id = dto.id,
                        hour = Hour(dto.hour.toInt()),
                        minute = Minute(dto.minute.toInt()),
                        weeklyRepeat = WeeklyRepeat(1, 2, 3, 4, 5, 6, 7),
                        label = Label(dto.label),
                        enabled = dto.enabled,
                        alertType = AlertType.SOUND_AND_VIBRATE,
                        ringtone = Ringtone(dto.ringtone),
                        alarmType = AlarmType.SAY_IT,
                        sayItScripts = SayItScripts("First script", "Second script", "Third script"),
                    ),
                ),
            )

            awaitComplete()
        }
    }

    @Test
    fun `When getAlarm is called it delegates the call to dataStore and map received dto to alarm`() = runTest {
        // Given
        val dto = getRandomDTO(
            weeklyRepeat = 1,
            alertType = 1,
            alarmType = 2,
            sayItScripts = "One,Two,Three",
        )
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = AlarmRepository(dataSource, dispatcher)

        coEvery { dataSource.getById(3) } returns Result.success(dto)

        // When
        val actual = repository.getAlarm(3, this)
            .await()
            .getOrNull()

        // Then
        actual mustBe
            Alarm(
                id = dto.id,
                hour = Hour(dto.hour.toInt()),
                minute = Minute(dto.minute.toInt()),
                weeklyRepeat = WeeklyRepeat(1),
                label = Label(dto.label),
                enabled = dto.enabled,
                alertType = AlertType.VIBRATE_ONLY,
                ringtone = Ringtone(dto.ringtone),
                alarmType = AlarmType.PUSH_BUTTON,
                sayItScripts = SayItScripts("One", "Two", "Three"),
            )
    }

    @Test
    fun `When getAlarm is called and alarm is not found it returns failure`() = runTest {
        // Given
        val result = Result.failure<AlarmDTO>(IllegalStateException())
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = AlarmRepository(dataSource, dispatcher)

        coEvery { dataSource.getById(any()) } returns result

        // When
        val actual = repository.getAlarm(3, this).await()

        // Then
        actual mustBe result
    }

    @Test
    fun `When save is called it invoke dataStore insert`() = runTest {
        // Given
        val alarm = getRandomDTO().toAlarm()
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = AlarmRepository(dataSource, dispatcher)

        // When
        repository.save(alarm, this)
        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.insert(any()) }
    }

    @Test
    fun `When update is called it invoke dataStore update`() = runTest {
        // Given
        val alarm = getRandomDTO().toAlarm()
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = AlarmRepository(dataSource, dispatcher)

        // When
        repository.update(alarm, this)
        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.update(any()) }
    }

    @Test
    fun `When updateEnabled is called it invoke dataStore updateEnabled`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = AlarmRepository(dataSource, dispatcher)

        // When
        repository.updateEnabled(3L, true, this)
        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.updateEnabled(any(), any()) }
    }

    @Test
    fun `When delete is called it invoke dataStore delete`() = runTest {
        // Given
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        val repository = AlarmRepository(dataSource, dispatcher)

        // When
        repository.delete(3, this)
        runCurrent()

        // Then
        coVerify(exactly = 1) { dataSource.delete(any()) }
    }

    @Test
    fun `It fulfills AlarmRepositoryContract`() {
        val dispatcher = StandardTestDispatcher()

        AlarmRepository(dataSource, dispatcher) fulfils
            RepositoryContract.AlarmRepository::class
    }

    private fun getRandomDTO(
        hour: Long = fixture.fixture(range = 0..23),
        minute: Long = fixture.fixture(range = 0..59),
        weeklyRepeat: Long = 85,
        label: String = fixture.fixture(),
        enabled: Boolean = fixture.fixture(),
        alertType: Long = fixture.fixture(range = 0..AlarmType.entries.lastIndex),
        ringtone: String = fixture.fixture(),
        alarmType: Long = fixture.fixture(range = 0..AlarmType.entries.lastIndex),
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
