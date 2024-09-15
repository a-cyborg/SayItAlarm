/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import android.Manifest
import android.app.AlarmManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.DialogStandardFitContent
import org.a_cyb.sayitalarm.design_system.atom.SpacerLarge
import org.a_cyb.sayitalarm.design_system.atom.TextButtonRequestPermission

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionLauncher() {
    val permissionState = rememberMultiplePermissionsState(permissions = getPermissions())
    var showRationale by rememberSaveable { mutableStateOf(permissionState.shouldShowRationale) }

    if (permissionState.shouldShowRationale) {
        PermissionRationaleDialog(
            onDismiss = { showRationale = false },
            onRequest = {
                showRationale = false
                permissionState.launchMultiplePermissionRequest()
            }
        )
    } else if (!permissionState.allPermissionsGranted) {
        LaunchedEffect(Unit) {
            permissionState.launchMultiplePermissionRequest()
        }
    }
}

@Composable
private fun getPermissions(): List<String> {
    val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val alarmManager = LocalContext.current.getSystemService(AlarmManager::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        permissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
    }

    return permissions
}

@Composable
fun PermissionRationaleDialog(
    onDismiss: () -> Unit,
    onRequest: () -> Unit,
) {
    DialogStandardFitContent(onDismiss = onDismiss) {
        TextBoxTitleAndInfo(
            title = stringResource(id = R.string.permission),
            info = stringResource(id = R.string.info_permission_rationale),
        )
        SpacerLarge()
        TextButtonRequestPermission(onClick = onRequest)
    }
}
