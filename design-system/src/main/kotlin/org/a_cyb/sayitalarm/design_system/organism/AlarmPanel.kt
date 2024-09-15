/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.organism

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenStandardScrollable
import org.a_cyb.sayitalarm.design_system.atom.DividerStandard
import org.a_cyb.sayitalarm.design_system.atom.IconButtonAdd
import org.a_cyb.sayitalarm.design_system.atom.IconButtonInfo
import org.a_cyb.sayitalarm.design_system.atom.PanelStandard
import org.a_cyb.sayitalarm.design_system.atom.SpacerLarge
import org.a_cyb.sayitalarm.design_system.atom.SpacerMedium
import org.a_cyb.sayitalarm.design_system.atom.SpacerSmall
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.design_system.molecule.ActionRowCollapse
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemClickableBordered
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemStandardClickable
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemStandardLarge
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemWithPopupPicker
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemWithPopupPickerStandardWheel
import org.a_cyb.sayitalarm.design_system.molecule.PopupPickerLabel
import org.a_cyb.sayitalarm.design_system.molecule.PopupPickerRepeat
import org.a_cyb.sayitalarm.design_system.molecule.PopupPickerRingtone
import org.a_cyb.sayitalarm.design_system.molecule.PopupPickerSayItScript
import org.a_cyb.sayitalarm.design_system.molecule.PopupPickerTime
import org.a_cyb.sayitalarm.design_system.molecule.TextBoxInfo
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.contracts.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.contracts.command.SetAlertTypeCommand
import org.a_cyb.sayitalarm.presentation.contracts.command.SetLabelCommand
import org.a_cyb.sayitalarm.presentation.contracts.command.SetRingtoneCommand
import org.a_cyb.sayitalarm.presentation.contracts.command.SetScriptsCommand
import org.a_cyb.sayitalarm.presentation.contracts.command.SetTimeCommand
import org.a_cyb.sayitalarm.presentation.contracts.command.SetWeeklyRepeatCommand

@Composable
fun AlarmPanel(
    alarmUI: AlarmUI,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    ColumnScreenStandardScrollable {
        TimePanel(
            time = alarmUI.timeUI,
            onConfirm = { hour, minute ->
                executor(SetTimeCommand(Hour(hour), Minute(minute)))
            },
        )
        SpacerLarge()
        AdvancedConfigurationPanel(
            repeat = alarmUI.weeklyRepeatUI,
            label = alarmUI.label,
            ringtoneUI = alarmUI.ringtoneUI,
            alertTypeUI = alarmUI.alertTypeUI,
            executor = executor,
        )
        SpacerLarge()
        SayItScriptsPanel(
            value = alarmUI.sayItScripts,
            onConfirm = { scripts ->
                executor(SetScriptsCommand(SayItScripts(scripts)))
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePanel(
    time: TimeUI,
    onConfirm: (Int, Int) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute,
    )
    var showPopUpPicker by rememberSaveable { mutableStateOf(false) }

    PanelStandard {
        PanelItemClickableBordered(
            contentDescription = stringResource(id = R.string.action_set_alarm_time),
            onClick = { showPopUpPicker = true },
        ) {
            TextDisplayStandardLarge(text = time.formattedTime)
        }
    }

    if (showPopUpPicker) {
        PopupPickerTime(
            hour = timePickerState.hour,
            minute = timePickerState.minute,
            onConfirm = onConfirm,
            onCancel = { showPopUpPicker = false },
        )
    }
}

@Composable
private fun AdvancedConfigurationPanel(
    repeat: WeeklyRepeatUI,
    label: String,
    ringtoneUI: RingtoneUI,
    alertTypeUI: AlarmPanelContract.AlertTypeUI,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelStandard(
        { PanelItemLabel(label = label, executor = executor) },
        { PanelItemRepeat(repeat = repeat, executor = executor) },
        { PanelItemRingtone(ringtoneUI = ringtoneUI, executor = executor) },
        { PanelItemAlertType(alertTypeUI = alertTypeUI, executor = executor) },
    )
}

@Composable
private fun PanelItemLabel(
    label: String,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelItemWithPopupPicker(
        valueLabel = stringResource(id = R.string.label),
        value = label,
    ) { onCancel ->
        PopupPickerLabel(
            label = label,
            onConfirm = { executor(SetLabelCommand(it)) },
            onCancel = onCancel,
        )
    }
}

@Composable
private fun PanelItemRepeat(
    repeat: WeeklyRepeatUI,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelItemWithPopupPicker(
        valueLabel = stringResource(id = R.string.repeat),
        value = repeat.formatted,
    ) { onCancel ->
        PopupPickerRepeat(
            title = stringResource(id = R.string.repeat),
            selectableRepeats = repeat.selectableRepeats,
            onConfirm = { executor(SetWeeklyRepeatCommand(it)) },
            onCancel = onCancel,
        )
    }
}

@Composable
private fun PanelItemRingtone(
    ringtoneUI: RingtoneUI,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelItemWithPopupPicker(
        valueLabel = stringResource(id = R.string.ringtone),
        value = ringtoneUI.title,
    ) { onCancel ->
        PopupPickerRingtone(
            selectedUri = ringtoneUI.uri,
            onConfirm = { title, uri ->
                executor(SetRingtoneCommand(RingtoneUI(title, uri.toString())))
            },
            onCancel = onCancel,
        )
    }
}

@Composable
private fun PanelItemAlertType(
    alertTypeUI: AlarmPanelContract.AlertTypeUI,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    val selectableTypes = alertTypeUI.selectableAlertType

    val wheelPickerValues = selectableTypes.map { it.name }
    val selectedIdx = selectableTypes.indexOfFirst { it.selected }

    PanelItemWithPopupPickerStandardWheel(
        title = stringResource(id = R.string.alert_type),
        values = wheelPickerValues,
        pickerItemRow = { TextTitleStandardLarge(text = it) },
        selectedItemIdx = selectedIdx,
        popUpPickerOnConfirm = { idx ->
            val selected = selectableTypes[idx].name
            executor(SetAlertTypeCommand(selected))
        },
    )
}

@Composable
private fun SayItScriptsPanel(
    value: List<String>,
    onConfirm: (List<String>) -> Unit,
) {
    val scripts = remember { value.toMutableStateList() }
    val selectedIdx = remember { mutableIntStateOf(0) }

    var showInfoText by rememberSaveable { mutableStateOf(scripts.isEmpty()) } // If scripts is empty show info text.
    var showPopUpPicker by rememberSaveable { mutableStateOf(false) }

    PanelStandard {
        PanelItemStandardLarge(valueLabel = stringResource(id = R.string.say_it)) {
            IconButtonInfo { showInfoText = !showInfoText }
        }

        AnimatedVisibility(showInfoText) {
            Column {
                TextBoxInfo(text = stringResource(id = R.string.info_scripts))
                ActionRowCollapse { showInfoText = false }
            }
        }

        scripts.mapIndexed { index, script ->
            PanelItemStandardClickable(value = script) {
                selectedIdx.intValue = index
                showPopUpPicker = true
            }
            SpacerSmall()
            DividerStandard()
            SpacerMedium()
        }

        IconButtonAdd(contentDescription = stringResource(id = R.string.action_add_script)) {
            selectedIdx.intValue = scripts.lastIndex + 1
            showPopUpPicker = true
        }

        if (showPopUpPicker) {
            PopupPickerSayItScript(
                script = scripts.getOrNull(selectedIdx.intValue) ?: "",
                onConfirm = { script ->
                    if (scripts.getOrNull(selectedIdx.intValue) == null) {
                        scripts.add(script)
                    } else {
                        scripts[selectedIdx.intValue] = script
                    }

                    onConfirm(scripts)
                },
                onCancel = { showPopUpPicker = false },
                onDelete = {
                    scripts.removeAt(selectedIdx.intValue)
                    onConfirm(scripts)
                },
            )
        }
    }
}
