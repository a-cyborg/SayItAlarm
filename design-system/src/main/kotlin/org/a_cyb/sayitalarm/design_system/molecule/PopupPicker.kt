/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.DialogStandardFitContent
import org.a_cyb.sayitalarm.design_system.atom.DialogStandardFitContentScrollable
import org.a_cyb.sayitalarm.design_system.atom.PanelStandardLazy
import org.a_cyb.sayitalarm.design_system.atom.SpacerMedium
import org.a_cyb.sayitalarm.design_system.atom.TextButtonDelete
import org.a_cyb.sayitalarm.design_system.atom.TextFieldLabel
import org.a_cyb.sayitalarm.design_system.atom.TextFieldSayItScript
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.SelectableRepeat

@Composable
fun <T> PopUpPickerStandardWheel(
    title: String,
    info: String = "",
    pickerValues: List<T>,
    pickerInitIdx: Int = 0,
    pickerItemRow: @Composable (T) -> Unit,
    onConfirm: (idx: Int) -> Unit,
    onCancel: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    DialogStandardFitContent(onDismiss = onCancel) {
        TextBoxTitleAndInfo(title = title, info = info)
        SpacerMedium()
        Box {
            WheelPicker(
                values = pickerValues,
                initIdx = pickerInitIdx,
                itemRow = { pickerItemRow(it) },
                onCancel = onCancel,
                onConfirm = {
                    scope.launch { onConfirm(it) }
                        .invokeOnCompletion { onCancel() }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupPickerTime(
    hour: Int,
    minute: Int,
    onConfirm: (Int, Int) -> Unit,
    onCancel: () -> Unit,
) {
    val timePickerState = rememberTimePickerState(initialHour = hour, initialMinute = minute)

    DialogStandardFitContentScrollable(onDismiss = onCancel) {
        TimePicker(state = timePickerState)
        ActionRowCancelAndConfirm(
            onCancel = onCancel,
            onConfirm = {
                onConfirm(timePickerState.hour, timePickerState.minute)
                onCancel()
            },
        )
    }
}

@Composable
fun PopupPickerRepeat(
    title: String,
    selectableRepeats: List<SelectableRepeat>,
    onConfirm: (List<SelectableRepeat>) -> Unit,
    onCancel: () -> Unit
) {
    val selections: MutableMap<String, Boolean> = remember {
        mutableStateMapOf(*selectableRepeats.map { it.name to it.selected }.toTypedArray())
    }

    DialogStandardFitContent(onDismiss = onCancel) {
        PanelStandardLazy {
            item { TextBoxTitleAndInfo(title = title, info = "") }
            items(selectableRepeats) {
                PanelItemStandard(valueLabel = it.name) {
                    RadioButton(
                        selected = selections[it.name] ?: false,
                        onClick = {
                            when (selections[it.name]) {
                                true -> selections[it.name] = false
                                else -> selections[it.name] = true
                            }
                        }
                    )
                }
            }
            item {
                SpacerMedium()
                ActionRowCancelAndConfirm(
                    onCancel = onCancel,
                    onConfirm = {
                        val selected = selectableRepeats.map { selectable ->
                            selectable.copy(selected = selections[selectable.name] ?: false)
                        }

                        onConfirm(selected)
                        onCancel()
                    }
                )
            }
        }
    }
}

@Composable
fun PopupPickerRingtone(
    selectedUri: String,
    onConfirm: (title: String, Uri) -> Unit,
    onCancel: () -> Unit,
) {
    // TODO: All below logic should be moved to ringtoneManager module.
    //  Receive all selectable ringtone and view should only displays received lists.
    val context = LocalContext.current
    val ringtonePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    }

                    uri?.let {
                        val title = RingtoneManager.getRingtone(context, uri).getTitle(context)
                        onConfirm(title, it)
                    }
                }
                onCancel()
            }
        )

    val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL)
        .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        .putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedUri.toUri())

    SideEffect {
        ringtonePickerLauncher.launch(intent)
    }
}

@Composable
fun PopupPickerLabel(
    label: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit,
) {
    var text by rememberSaveable(label) { mutableStateOf(label) }

    DialogStandardFitContent(onDismiss = onCancel) {
        TextTitleStandardLarge(text = stringResource(id = R.string.label))
        SpacerMedium()
        TextFieldLabel(text = text) { text = it }
        SpacerMedium()
        ActionRowCancelAndConfirm(
            onCancel = onCancel,
            onConfirm = {
                onConfirm(text)
                onCancel()
            }
        )
    }
}

@Composable
fun PopupPickerSayItScript(
    script: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    var text by rememberSaveable { mutableStateOf(script) }

    DialogStandardFitContentScrollable(onDismiss = onCancel) {
        TextTitleStandardLarge(text = stringResource(id = R.string.say_it))
        SpacerMedium()
        TextFieldSayItScript(
            text = text,
            onValueChange = { inputStr ->
                text = inputStr.filter { it.isLetter() || it.isWhitespace() }
            }
        )
        SpacerMedium()
        ActionRowCancelAndConfirm(
            onCancel = onCancel,
            onConfirm = {
                onConfirm(text)
                onCancel()
            }
        )
        SpacerMedium()
        TextBoxWarningTitle(text = stringResource(id = R.string.info_scripts_only_letter))
        TextButtonDelete {
            onDelete()
            onCancel()
        }
    }
}
