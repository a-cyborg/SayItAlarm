package org.a_cyb.sayitalarm.feature.alarm

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.core.alarm.AlarmService
import org.a_cyb.sayitalarm.core.data.repository.UserDataRepository
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme
import org.a_cyb.sayitalarm.util.TAG
import org.a_cyb.sayitalarm.util.isBuildVersionOMR1OrLater
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : ComponentActivity() {

    @Inject lateinit var userDataRepo: UserDataRepository

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action: String? = intent?.action

            if (!isAlarmHandled) {
                when (action) {
                    AlarmService.ALARM_SNOOZE_ACTION -> snooze()
                }
            }
        }
    }

    private var isAlarmHandled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: [***] AlarmActivity")

        volumeControlStream = AudioManager.STREAM_ALARM
        // Get alarm Id with
//        val instanceId = AlarmInstance.getId(getIntent().getData()!!)
//        Log.i("Displaying alarm for instance: %s", mAlarmInstance)

        when {
            isBuildVersionOMR1OrLater -> {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
                (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)
                    .requestDismissKeyguard(this, null)
            }
            else -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
            }
        }

        hideNavigationBar()

//        resources.getBoolean(R.bool.rotateAlarmAlert)
        setContent {
            SayItAlarmTheme {
                AlarmScreen()
            }
        }
    }

    override fun onResume() {
        // TODO: Bind service
        super.onResume()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Don't allow back to dismiss.
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: [***] AlarmActivity is destroyed")
        super.onDestroy()
    }

    private fun hideNavigationBar() {
        // Also can be implemented on Composable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                    hide(WindowInsets.Type.navigationBars())
                }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun snooze() {
        isAlarmHandled = true

        // I think I should handle this business logic on ViewModel.
        // And ViewModel updates the event. And Activity Collect event and handling
        // AlarmService and AlarmScheduler.

        val snoozeMinutes = lifecycleScope.launch {
            userDataRepo.userData.first().snoozeLength
        }
        fun getSnoozeMinutes(): Int {
            var snoozeLength: Int = 0
            lifecycleScope.launch {
                snoozeLength = userDataRepo.userData.first().snoozeLength
            }
            return snoozeLength
        }

        val newAlarmTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, getSnoozeMinutes())
        }
        // Viewmodel or screen shows the snooze state.
    }
}


