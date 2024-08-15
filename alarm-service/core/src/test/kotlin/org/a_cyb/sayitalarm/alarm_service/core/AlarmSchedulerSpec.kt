/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import kotlinx.coroutines.test.runTest
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmSchedulerSpec {
    private lateinit var context: Context
    private lateinit var configuration: Configuration
    private lateinit var workManager: WorkManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        configuration = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
        workManager = WorkManager.getInstance(context)
    }

    @Test
    fun `When scheduleAllAlarms is called it enqueues AlarmSchedulerWorker`() = runTest {
        // Given
        val scheduler = AlarmScheduler(context)

        // When
        scheduler.scheduleAlarms(this)

        val workInfo = WorkManager.getInstance(context)
            .getWorkInfosByTag(AlarmSchedulerWorker::class.qualifiedName!!)
            .get()

        // Then
        workInfo.isEmpty() mustBe false
        workInfo.size mustBe 1
    }

    @Test
    fun `When scheduleSnooze is called it enqueues AlarmSchedulerWorker`() = runTest {
        // Given
        val scheduler = AlarmScheduler(context)

        // When
        scheduler.scheduleSnooze(3L, Snooze(15))

        // Then
        val workInfo = WorkManager.getInstance(context)
            .getWorkInfosByTag(AlarmSchedulerWorker::class.qualifiedName!!)
            .get()

        workInfo.isEmpty() mustBe false
        workInfo.size mustBe 1
    }
}
