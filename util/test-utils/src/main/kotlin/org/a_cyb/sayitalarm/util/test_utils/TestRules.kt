/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.util.test_utils

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.ExternalResource
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.robolectric.Shadows.shadowOf

fun createAddActivityToRobolectricRule(): TestWatcher = object : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        val appContext: Application = ApplicationProvider.getApplicationContext()
        val activityInfo = ActivityInfo().apply {
            name = ComponentActivity::class.java.name
            packageName = appContext.packageName
        }
        shadowOf(appContext.packageManager).addOrUpdateActivity(activityInfo)
    }
}

fun createKoinExternalResourceRule(module: Module): ExternalResource = object : ExternalResource() {
    override fun before() {
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
                modules(module)
            }
        } else {
            loadKoinModules(module)
        }
    }

    override fun after() {
        stopKoin()
    }
}
