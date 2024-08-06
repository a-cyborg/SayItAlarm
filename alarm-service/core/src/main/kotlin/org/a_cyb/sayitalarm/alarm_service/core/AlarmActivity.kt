/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core

import android.app.KeyguardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.a_cyb.sayitalarm.design_system.screen.AlarmScreen
import org.a_cyb.sayitalarm.domain.alarm_service.AlarmServiceContract.AlarmServiceController
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

class AlarmActivity : ComponentActivity() {

    private val controller: AlarmServiceController = get(AlarmServiceController::class.java)

    private lateinit var alarmData: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        alarmData = intent.extras ?: Bundle()
        setupTurnScreenOn()

        setContent {
            AlarmScreen(
                getViewModel<AlarmViewModel> {
                    parametersOf(controller)
                }
            )
        }
    }

    private fun setupTurnScreenOn() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(KEYGUARD_SERVICE) as KeyguardManager)
                .requestDismissKeyguard(this, null)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val alarmService = (service as AlarmService.AlertServiceBinder).getService()
            controller.onServiceBind(alarmService)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            controller.onServiceDisconnected()
        }
    }

    override fun onStart() {
        super.onStart()

        bindService(
            getAlarmServiceBindIntent(),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun getAlarmServiceBindIntent(): Intent {
        val ringtone = alarmData.getString(AlarmScheduler.BUNDLE_KEY_RINGTONE_URI)
        val alertType = alarmData.getInt(AlarmScheduler.BUNDLE_KEY_ALERT_TYPE)

        return Intent(this, AlarmService::class.java)
            .putExtra(AlarmService.SERVICE_BIND_EXTRA_RINGTONE_URI, ringtone)
            .putExtra(AlarmService.SERVICE_BIND_EXTRA_ALERT_TYPE, alertType)
    }

    override fun onDestroy() {
        super.onDestroy()

        window.clearFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false)
            setTurnScreenOn(false)
        }

        unbindService(serviceConnection)
    }
}