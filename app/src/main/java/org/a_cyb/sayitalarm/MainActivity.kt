package org.a_cyb.sayitalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            SayItAlarmTheme {
                SiaApp(
//                    windowSizeClass = calculateWindowSizeClass(activity = this),
                )
            }
        }
    }
}