/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

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
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.ColumnScreenStandardScrollable
import org.a_cyb.sayitalarm.atom.DialogStandardFillMax
import org.a_cyb.sayitalarm.atom.DialogStandardFitContent
import org.a_cyb.sayitalarm.atom.IconButtonDeleteText
import org.a_cyb.sayitalarm.atom.PanelStandardLazy
import org.a_cyb.sayitalarm.atom.SpacerLarge
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.SpacerXLarge
import org.a_cyb.sayitalarm.atom.TextFieldSayItScript
import org.a_cyb.sayitalarm.atom.TextTitleStandardLarge

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
        TextRowTitleAndInfo(title = title, info = info)
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
    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute
    )

    DialogStandardFitContent(onDismiss = onCancel) {
        ColumnScreenStandardScrollable {
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
}

@Composable
fun PopupPickerRepeat(
    title: String,
    selectedRepeat: Set<Int>,
    selectableRepeat: Map<String, Int>,
    onConfirm: (Set<Int>) -> Unit,
    onCancel: () -> Unit
) {
    val selections: MutableMap<String, Boolean> = remember {
        mutableStateMapOf(
            *selectableRepeat.keys.map {
                it to selectedRepeat.contains(selectableRepeat[it])
            }.toTypedArray()
        )
    }

    DialogStandardFitContent(onDismiss = onCancel) {
        PanelStandardLazy {
            item { TextRowTitleAndInfo(title = title, info = "") }
            items(selectableRepeat.keys.toList()) {
                PanelItemStandard(valueLabel = it) {
                    RadioButton(
                        selected = selections[it] ?: false,
                        onClick = {
                            when (selections[it]) {
                                true -> selections[it] = false
                                else -> selections[it] = true
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
                        val selected: Set<Int> = selections
                            .filterValues { it }
                            .keys
                            .map { selectableRepeat[it]!! }
                            .toSortedSet()

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
    onConfirm: (Uri) -> Unit,
    onCancel: () -> Unit,
) {
    // TODO: All below logic should be moved to ringtoneManager module.
    //  Receive all selectable ringtone and view should only displays received lists.
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

                    uri?.let(onConfirm)
                }
                onCancel()
            }
        )

    val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        .apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedUri.toUri())
        }

    SideEffect {
        ringtonePickerLauncher.launch(intent)
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

    DialogStandardFillMax(onDismiss = onCancel, topAppBar = {}) {
        ColumnScreenStandardScrollable {
            SpacerLarge()
            TextTitleStandardLarge(text = stringResource(id = R.string.say_it))
            SpacerMedium()
            TextFieldSayItScript(
                text = text,
                onValueChange = { it ->
                    text = it.filter { it.isLetter() || it.isWhitespace() }
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
            TextRowWarning(text = stringResource(id = R.string.info_scripts_only_letter))
            IconButtonDeleteText {
                onDelete()
                onCancel()
            }
            SpacerXLarge()
        }
    }
}
