/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.scheduler

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmSchedulerContract
import org.a_cyb.sayitalarm.entity.Snooze
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@RunWith(AndroidJUnit4::class)
class AlarmSchedulerSpec {

    private val fixture = kotlinFixture()
    private val workManager: WorkManager = mockk(relaxed = true)
    private lateinit var scheduler: AlarmSchedulerContract

    @Before
    fun setup() {
        scheduler = AlarmScheduler(ApplicationProvider.getApplicationContext())

        mockkStatic(WorkManager::class)
        every { WorkManager.getInstance(any()) } returns workManager
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When scheduleAllAlarms is called, it enqueues AlarmSchedulerWorker with SET_ALARM data`() {
        // Given
        val capturedRequest = slot<WorkRequest>()

        // When
        scheduler.scheduleAlarms()

        // Then
        verify(exactly = 1) { workManager.enqueue(capture(capturedRequest)) }

        with(capturedRequest.captured.workSpec) {
            assertFalse(isPeriodic)
            assertEquals(WorkInfo.State.ENQUEUED, state)
            assertEquals(AlarmSchedulerWorker::class.qualifiedName, workerClassName)
            assertEquals(
                AlarmScheduler.SCHEDULER_WORKER_WORK_SET_ALARM,
                input.getInt(AlarmScheduler.SCHEDULER_WORKER_WORK_TYPE, -1),
            )
        }
    }

    @Test
    fun `When scheduleSnooze is called, it enqueues AlarmSchedulerWorker with SET_SNOOZE data`() {
        // Given
        val capturedRequest = slot<WorkRequest>()
        val alarmId: Long = fixture.fixture(range = 1..Int.MAX_VALUE)
        val snooze = Snooze(fixture.fixture(range = 5..120))

        // When
        scheduler.scheduleSnooze(alarmId, snooze)

        // Then
        verify(exactly = 1) { workManager.enqueue(capture(capturedRequest)) }

        with(capturedRequest.captured.workSpec) {
            assertFalse(isPeriodic)
            assertEquals(AlarmSchedulerWorker::class.qualifiedName, workerClassName)
            assertEquals(
                AlarmScheduler.SCHEDULER_WORKER_WORK_SET_SNOOZE,
                input.getInt(AlarmScheduler.SCHEDULER_WORKER_WORK_TYPE, -1),
            )
            assertEquals(
                alarmId,
                input.getLong(AlarmScheduler.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, -1),
            )
            assertEquals(
                snooze.snooze,
                input.getInt(AlarmScheduler.SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN, -1),
            )
        }
    }

    @Test
    fun `When cancelAlarm is called, it enqueues AlarmSchedulerWorker with CANCEL_ALAARM data`() {
        // Given
        val capturedRequest = slot<WorkRequest>()
        val alarmId: Long = fixture.fixture(range = 1..Int.MAX_VALUE)

        // When
        scheduler.cancelAlarm(alarmId)

        // Then
        verify(exactly = 1) { workManager.enqueue(capture(capturedRequest)) }

        with(capturedRequest.captured.workSpec) {
            assertFalse(isPeriodic)
            assertEquals(AlarmSchedulerWorker::class.qualifiedName, workerClassName)
            assertEquals(
                AlarmScheduler.SCHEDULER_WORKER_WORK_CANCEL_ALARM,
                input.getInt(AlarmScheduler.SCHEDULER_WORKER_WORK_TYPE, -1),
            )
            assertEquals(
                alarmId,
                input.getLong(AlarmScheduler.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, -1),
            )
        }
    }
}
