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
import androidx.work.testing.WorkManagerTestInitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AlarmSchedulerSpec {

    private lateinit var context: Context
    private val workManagerConfig = Configuration.Builder()
        .setMinimumLoggingLevel(Log.DEBUG)
        .build()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, workManagerConfig)

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When setAlarm is called it enqueues AlarmSchedulerWorker`() = runTest {
        // Given
        val scheduler = AlarmScheduler(context)

        // When
        scheduler.setAlarm(this)

        val workInfo = WorkManager.getInstance(context)
            .getWorkInfosByTag(AlarmSchedulerWorker::class.qualifiedName!!)
            .get()

        // Then
        workInfo.isEmpty() mustBe false
        workInfo.size mustBe 1
    }
}