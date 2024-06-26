/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.organism

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
import androidx.compose.ui.text.style.TextAlign
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.ColumnScreenStandardScrollable
import org.a_cyb.sayitalarm.atom.DividerStandard
import org.a_cyb.sayitalarm.atom.IconButtonAdd
import org.a_cyb.sayitalarm.atom.IconButtonInfo
import org.a_cyb.sayitalarm.atom.PanelStandard
import org.a_cyb.sayitalarm.atom.SpacerLarge
import org.a_cyb.sayitalarm.atom.SpacerXLarge
import org.a_cyb.sayitalarm.atom.TextDisplayStandardLarge
import org.a_cyb.sayitalarm.atom.TextFieldStandard
import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute
import org.a_cyb.sayitalarm.entity.SayItScripts
import org.a_cyb.sayitalarm.molecule.ActionRowCollapse
import org.a_cyb.sayitalarm.molecule.PanelItemStandard
import org.a_cyb.sayitalarm.molecule.PanelItemStandardClickable
import org.a_cyb.sayitalarm.molecule.PanelItemStandardClickableBordered
import org.a_cyb.sayitalarm.molecule.PanelItemWithPopupPicker
import org.a_cyb.sayitalarm.molecule.PopupPickerRepeat
import org.a_cyb.sayitalarm.molecule.PopupPickerRingtone
import org.a_cyb.sayitalarm.molecule.PopupPickerSayItScript
import org.a_cyb.sayitalarm.molecule.PopupPickerTime
import org.a_cyb.sayitalarm.molecule.TextRowInfo
import org.a_cyb.sayitalarm.presentation.command.CommandContract
import org.a_cyb.sayitalarm.presentation.command.CommandContract.CommandReceiver
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.AlarmUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.RingtoneUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.TimeUI
import org.a_cyb.sayitalarm.presentation.AlarmPanelContract.WeeklyRepeatUI
import org.a_cyb.sayitalarm.presentation.command.SetLabelCommand
import org.a_cyb.sayitalarm.presentation.command.SetRingtoneCommand
import org.a_cyb.sayitalarm.presentation.command.SetScriptsCommand
import org.a_cyb.sayitalarm.presentation.command.SetTimeCommand
import org.a_cyb.sayitalarm.presentation.command.SetWeeklyRepeatCommand

@Composable
fun AlarmPanel(
    alarmUI: AlarmUI,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    ColumnScreenStandardScrollable {
        SpacerXLarge()
        TimePanel(
            time = alarmUI.timeUI,
            onConfirm = { hour, minute ->
                executor(SetTimeCommand(Hour(hour), Minute(minute)))
            }
        )
        SpacerLarge()
        AdvancedConfigurationPanel(
            repeat = alarmUI.weeklyRepeatUI,
            label = alarmUI.label,
            ringtone = alarmUI.ringtoneUI,
            executor = executor
        )
        SpacerLarge()
        SayItScriptsPanel(
            value = alarmUI.sayItScripts,
            onConfirm = { scripts ->
                executor(SetScriptsCommand(SayItScripts(scripts)))
            }
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
        PanelItemStandardClickableBordered(
            contentDescription = stringResource(id = R.string.action_set_alarm_time),
            onClick = { showPopUpPicker = true }
        ) {
            TextDisplayStandardLarge(text = time.formattedTime)
        }
    }

    if (showPopUpPicker) {
        PopupPickerTime(
            hour = timePickerState.hour,
            minute = timePickerState.minute,
            onConfirm = onConfirm,
            onCancel = { showPopUpPicker = false }
        )
    }
}

@Composable
private fun AdvancedConfigurationPanel(
    repeat: WeeklyRepeatUI,
    label: String,
    ringtone: RingtoneUI,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelStandard(
        { PanelItemLabel(label = label, executor = executor) },
        { PanelItemRepeat(repeat = repeat, executor = executor) },
        { PanelItemRingtone(ringtoneUI = ringtone, executor = executor) },
    )
}

@Composable
private fun PanelItemLabel(
    label: String,
    executor: (CommandContract.Command<out CommandReceiver>) -> Unit,
) {
    PanelItemStandard(valueLabel = stringResource(id = R.string.label)) {
        TextFieldStandard(
            value = label,
            hint = stringResource(id = R.string.label),
            textAlign = TextAlign.End,
            onDone = { executor(SetLabelCommand(it)) }
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
        value = repeat.selectableRepeats.first { it.selected }.name
    ) { onCancel ->
        PopupPickerRepeat(
            title = stringResource(id = R.string.repeat),
            selectableRepeats = repeat.selectableRepeats,
            onConfirm = { executor(SetWeeklyRepeatCommand(it)) },
            onCancel = onCancel
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
        value = ringtoneUI.title
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
private fun SayItScriptsPanel(
    value: List<String>,
    onConfirm: (List<String>) -> Unit,
) {
    val scripts = remember { value.toMutableStateList() }
    val selectedIdx = remember { mutableIntStateOf(0) }

    var showInfoText by rememberSaveable { mutableStateOf(false) }
    var showPopUpPicker by rememberSaveable { mutableStateOf(false) }

    PanelStandard {
        PanelItemStandard(valueLabel = stringResource(id = R.string.say_it)) {
            IconButtonInfo {
                showInfoText = !showInfoText
            }
        }

        if (showInfoText) {
            TextRowInfo(text = stringResource(id = R.string.info_scripts))
            ActionRowCollapse { showInfoText = false }
        }

        scripts.mapIndexed { index, script ->
            PanelItemStandardClickable(value = script) {
                selectedIdx.intValue = index
                showPopUpPicker = true
            }
            DividerStandard()
        }

        IconButtonAdd {
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
                }
            )
        }
    }
}
