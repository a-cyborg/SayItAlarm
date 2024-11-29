/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import org.a_cyb.sayitalarm.alarm_service.core.navigation.SiaAlarmServiceNavHost
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmControllerContract
import org.koin.android.ext.android.get

class AlarmActivity : ComponentActivity() {

    private val appExitReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == AlarmService.ACTION_EXIT_APP) {
                finishAndRemoveTask()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        registerExitReceiver()
        setupScreenOn()

        setContent {
            Color.useDarkTheme()
            window.statusBarColor = Color.surface.standard.toArgb()
            window.navigationBarColor = Color.surface.standard.toArgb()

            @SuppressLint("SourceLockedOrientationActivity")
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            SiaAlarmServiceNavHost()
        }
    }

    private fun registerExitReceiver() {
        ContextCompat.registerReceiver(
            this,
            appExitReceiver,
            IntentFilter().apply { addAction(AlarmService.ACTION_EXIT_APP) },
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    private fun setupScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        @Suppress("DEPRECATION")
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        )

        (getSystemService(KEYGUARD_SERVICE) as KeyguardManager)
            .requestDismissKeyguard(this, null)
    }

    override fun onStart() {
        super.onStart()

        bindService(
            Intent(this, AlarmService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE,
        )
    }

    private val alarmController: AlarmControllerContract = get()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val alarmService = (service as AlarmService.AlarmServiceBinder)
            val alarmId = service.getAlarmId()

            (alarmController as AlarmController)
                .connectService(alarmService, alarmId)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            (alarmController as AlarmController)
                .serviceDisconnected()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        alarmController.stopAlarm()
        unbindService(serviceConnection)
        unregisterReceiver(appExitReceiver)
        tearDownScreenOn()
    }

    private fun tearDownScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false)
            setTurnScreenOn(false)
        }

        @Suppress("DEPRECATION")
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
        )
    }
}
