package org.a_cyb.sayitalarm

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import org.a_cyb.sayitalarm.core.alarm.ImplAlarmScheduler
import org.a_cyb.sayitalarm.core.designsystem.component.SiaIcons
import org.a_cyb.sayitalarm.core.designsystem.component.SiaTopAppBar
import org.a_cyb.sayitalarm.core.designsystem.component.SiaTopAppBarIconButton
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme
import org.a_cyb.sayitalarm.util.getRandomAlarm

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SiaApp() {
    val snackBarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    var onEditMode by rememberSaveable { mutableStateOf(false) }
    // [***] DEV Mode
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            SiaTopAppBar(
                titleResId = R.string.app_name,
                navigationItem = {
                    SiaTopAppBarIconButton(
                        onClick = { onEditMode = !onEditMode },
                        buttonTextResId = when (onEditMode) {
                            true -> R.string.sia_app_topbar_done_icon_text
                            false -> R.string.sia_app_topbar_edit_icon_text
                        }
                    )
                },
                firstActionItem = {
                    SiaTopAppBarIconButton(
                        onClick = { navController.navigateToAdd() },
                        icon = SiaIcons.Add,
                        contentDescriptionRes = R.string.sia_app_topbar_add_icon_description
                    )
                },
                secondActionItem = {
                    SiaTopAppBarIconButton(
                        onClick = {
                            // DEBUG
                            ImplAlarmScheduler.setAlarm(context, getRandomAlarm())
//                            navController.navigateToSettings()
                        },
                        icon = SiaIcons.Settings,
                        contentDescriptionRes = R.string.sia_app_topbar_settings_icon_description
                    )
                },
            )
        },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            SiaNavHost(
                navController = navController,
                onEditMode = onEditMode,
                offEditMode = { onEditMode = false },
            )
            NotificationPermissionEffect()
        }
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionEffect() {
    if (LocalInspectionMode.current) return  // Return if it is preview mode.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
    LaunchedEffect(notificationPermissionState) {
        val status = notificationPermissionState.status
        if (status is PermissionStatus.Denied && !status.shouldShowRationale) {
            notificationPermissionState.launchPermissionRequest()
        }
    }
    // TODO: When status is denied or shouldShowRationale display permission warning box.
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewTaAppScreen() {
    SayItAlarmTheme {
        SiaApp()
    }
}