/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlertServiceSpec {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `When onStartCommand is invoked it displays foreground notification`() {
    }
}
// val alertServiceIntent =
//     Intent(context, AlertService::class.java)
//         .putExtras(intent.extras!!)
//
// ContextCompat.startForegroundService(
//     context,
//     alertServiceIntent
// )
