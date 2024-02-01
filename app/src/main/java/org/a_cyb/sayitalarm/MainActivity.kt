package org.a_cyb.sayitalarm

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme
import org.a_cyb.sayitalarm.navigation.SiaApp
import org.a_cyb.sayitalarm.util.TAG

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Check permissions
        val permissionsGranted = checkPermissions()
        Log.d(TAG, "onCreate: permissionsGranted = $permissionsGranted")

//        ImplAlarmScheduler.setAlarm(this, getRandomAlarm())
        setContent {
            SayItAlarmTheme {
                SiaApp(
                    permissionsGranted,
//                    windowSizeClass = calculateWindowSizeClass(activity = this),
                )
            }
        }
    }

    private fun checkPermissions(): Boolean {
        var allPermissionsGranted: Boolean = true

        val permissions = arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.USE_FULL_SCREEN_INTENT,
        )

        fun requestPermission(callback: (Boolean)-> Unit) = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { callback(it) }

        permissions.forEach { permission ->
            when {
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED -> {
                    requestPermission { isGranted -> if (!isGranted) allPermissionsGranted = false }
                        .launch(permission)
                }
                ActivityCompat.shouldShowRequestPermissionRationale(this, permission)  -> {
                    /* Display rationale */
                }
                else -> { /* [no-ops] Permission is already granted. */ }
            }
        }

        return allPermissionsGranted
    }
}

/*
        // Permissions.
//        val requestPermissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//            when(isGranted) {
//                true -> {}
//                false -> {}
//            }
//        }
//
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED -> {/* Permission is granted. No ops. */}
//            ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                android.Manifest.permission.POST_NOTIFICATIONS,
//            )  -> { /* Display rationale */}
//            else -> {
//                requestPermissionLauncher.launch(
//                    android.Manifest.permission.POST_NOTIFICATIONS
//                )
//            }
//
//        }
 */