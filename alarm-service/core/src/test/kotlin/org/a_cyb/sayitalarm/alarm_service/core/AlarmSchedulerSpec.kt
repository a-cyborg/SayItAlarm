/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import androidx.work.WorkRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture

@RunWith(AndroidJUnit4::class)
class AlarmSchedulerSpec {
    private lateinit var context: Context
    private lateinit var workManager: WorkManager

    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        workManager = mockk(relaxed = true)

        mockkStatic(WorkManager::class)
        every { WorkManager.getInstance(any()) } returns workManager
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When scheduleAllAlarms is called it enqueues AlarmSchedulerWorker`() = runTest {
        // Given
        val scheduler = AlarmScheduler(context)
        val capturedRequest = slot<WorkRequest>()

        // When
        scheduler.scheduleAlarms(this)

        // Then
        verify(exactly = 1) { workManager.enqueue(capture(capturedRequest)) }

        val actualWorkSpec = capturedRequest.captured.workSpec
        val actualWorkType = actualWorkSpec.input.getInt(AlarmScheduler.SCHEDULER_WORKER_WORK_TYPE, 0)

        actualWorkSpec.isPeriodic mustBe false
        actualWorkType mustBe AlarmScheduler.SCHEDULER_WORKER_WORK_SET_ALARM
    }

    @Test
    fun `When scheduleSnooze is called it enqueues AlarmSchedulerWorker`() = runTest {
        // Given
        val scheduler = AlarmScheduler(context)
        val capturedRequest = slot<WorkRequest>()
        val alarmId: Long = fixture.fixture(range = 1..Int.MAX_VALUE)
        val snooze = Snooze(fixture.fixture(range = 5..120))

        // When
        scheduler.scheduleSnooze(alarmId, snooze)

        // Then
        verify(exactly = 1) { workManager.enqueue(capture(capturedRequest)) }

        val actualWorkSpec = capturedRequest.captured.workSpec
        val actualInput = actualWorkSpec.input
        val actualWorkType = actualInput.getInt(AlarmScheduler.SCHEDULER_WORKER_WORK_TYPE, 0)
        val actualAlarmId = actualInput.getLong(AlarmScheduler.SCHEDULER_WORKER_INPUT_DATA_ALARM_ID, 0)
        val actualSnoozeMin = actualInput.getInt(AlarmScheduler.SCHEDULER_WORKER_INPUT_DATA_SNOOZE_MIN, 0)

        actualWorkSpec.isPeriodic mustBe false
        actualWorkType mustBe AlarmScheduler.SCHEDULER_WORKER_WORK_SET_SNOOZE
        actualAlarmId mustBe alarmId
        actualSnoozeMin mustBe snooze.snooze
    }
}
