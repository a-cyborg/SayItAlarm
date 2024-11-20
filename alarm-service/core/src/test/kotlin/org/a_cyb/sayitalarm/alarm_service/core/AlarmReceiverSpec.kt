/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.a_cyb.sayitalarm.alarm_service.core.AlarmReceiver.Companion.INTENT_ACTION_DELIVER_ALARM
import org.a_cyb.sayitalarm.alarm_service.core.util.AlarmAlertWakeLock
import org.junit.Before
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class AlarmReceiverSpec {

    private lateinit var context: Context

    @Before
    fun setup() {
        mockkStatic(ContextCompat::class, AlarmAlertWakeLock::class)

        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `When onReceive is called with ACTION_DELIVER_ALARM action, it starts the AlarmService`() {
        // Given
        val captureIntent = slot<Intent>()
        val intent = Intent(INTENT_ACTION_DELIVER_ALARM)
            .putExtra(AlarmReceiver.INTENT_EXTRA_ALARM_ID, 33L)

        // When
        AlarmReceiver().onReceive(context, intent)

        // Then
        verify { ContextCompat.startForegroundService(any(), capture(captureIntent)) }

        assertEquals(
            AlarmService::class.qualifiedName,
            captureIntent.captured.component!!.className,
        )
        assertEquals(
            33,
            captureIntent.captured.getLongExtra(AlarmReceiver.INTENT_EXTRA_ALARM_ID, 0),
        )
    }

    @Test
    fun `It is declared to handle the intent action DELIVER_ALARM`() {
        val resolvedInfo = context.packageManager
            .queryBroadcastReceivers(
                Intent("org.a_cyb.sayitalarm.DELIVER_ALARM"),
                0,
            )
            .find { it.activityInfo.name == AlarmReceiver::class.qualifiedName }

        assertNotNull(resolvedInfo)
    }
}
