package org.a_cyb.sayitalarm.feature.alarm

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.a_cyb.sayitalarm.core.alarm.AlarmNotification
import org.a_cyb.sayitalarm.util.TAG

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val currentView = LocalView.current
    val context = LocalContext.current

    DisposableEffect(Unit) {
        // Keep the screen on.
        currentView.keepScreenOn = true
        // Lock screen orientation.
//        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onDispose {
            currentView.keepScreenOn = false
        }

    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "AlarmOffScreen")
                Text(text = "AlarmOffScreen")
                Text(text = "AlarmOffScreen")
                Text(text = "AlarmOffScreen")

                Spacer(modifier = Modifier.height(30.dp))

                Button(onClick = { Log.d(TAG, "AlarmScreen: DEBUG-COUNTER button on click") }) {
                    Text(text = "DEBUG-COUNTER")
                }

                Spacer(modifier = Modifier.height(13.dp))
                Button(onClick = { viewModel.onSayItButtonClicked() }) {
                    Text(text = "Say It")

                }

                val activity = (LocalContext.current as Activity)
                // Finish the activity
                Button(
                    onClick = {
                        AlarmNotification.cancelAlarmFiringNotification(context)
                        activity.finish()
                    }) {
                    Text(text = "Exit")
                }
            }
        }
    }
}

/*
@Composable
fun KeepScreenOn() {
    val currentView = LocalView.current
    DisposableEffect(Unit) {
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }
}
 */