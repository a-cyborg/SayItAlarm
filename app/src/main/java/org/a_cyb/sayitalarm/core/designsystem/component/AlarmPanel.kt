package org.a_cyb.sayitalarm.core.designsystem.component

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import org.a_cyb.sayitalarm.util.getFormattedClockTime
import org.a_cyb.sayitalarm.util.getLocalizedFullWeekdaysMap
import org.a_cyb.sayitalarm.util.getLocalizedShortWeekDayFormatted

@Composable
fun AlarmPanel(
    alarm: Alarm,
    onUpdateAlarmTime: (Int, Int) -> Unit,
    onUpdateWeeklyRepeat: (Set<Int>) -> Unit,
    onUpdateLabel: (String) -> Unit,
    onUpdateRingtone: (Uri) -> Unit,
    onUpdateSayItText: (List<String>) -> Unit,
) {
    val keyboardFocusManager = LocalFocusManager.current

    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showWeeklyRepeatPicker by rememberSaveable { mutableStateOf(false) }
    var showRingtonePicker by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .pointerInput(Unit) { detectTapGestures { keyboardFocusManager.clearFocus() } }
            .testTag("AlarmPanel"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TimeSection(
            time = alarm.combinedMinutes,
            onClick = { showTimePicker = true }
        )
        Spacer(modifier = Modifier.height(13.dp))
        // Section container.
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(13.dp))
                .padding(top = 13.dp, start = 13.dp, end = 13.dp, bottom = 19.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            WeeklyRepeatSection(
                weeklyRepeat = alarm.weeklyRepeat,
                onClick = { showWeeklyRepeatPicker = true },
            )
            Divider(Modifier.padding(vertical = 13.dp))
            LabelSection(
                label = alarm.label,
                onClick = onUpdateLabel,
                keyboardFocusManager = keyboardFocusManager,
            )
            Divider(Modifier.padding(vertical = 13.dp))
            RingtoneSection(
                ringtone = alarm.ringtone,
                onClick = { showRingtonePicker = true }
            )
        }
        Spacer(modifier = Modifier.height(13.dp))
        SayItTextSection(
            text = (alarm.alarmTerminator as VoiceRecognitionTerminator).scripts,
            onClick = { onUpdateSayItText(it) },
            keyboardFocusManager = keyboardFocusManager,
        )
    }

    when {
        showTimePicker -> {
            TimePicker(
                combinedMin = alarm.combinedMinutes,
                onCancel = { showTimePicker = false },
                onConfirm = onUpdateAlarmTime,
            )
        }
        showWeeklyRepeatPicker -> {
            WeeklyRepeatPicker(
                weeklyRepeat = alarm.weeklyRepeat,
                onCancel = { showWeeklyRepeatPicker = false },
                onConfirm = onUpdateWeeklyRepeat,
            )
        }
        showRingtonePicker -> {
            RingtonePicker(
                ringtone = alarm.ringtone,
                onCancel = { showRingtonePicker = false },
                onConfirm = onUpdateRingtone,
            )
        }
    }
}

@Composable
fun TimeSection(
    time: CombinedMinutes,
    onClick: () -> Unit,
) {
    val semanticsContentDescription = stringResource(id = R.string.alarm_panel_timepicker_open_button_description)

    Text(
        text = getFormattedClockTime(time),
        style = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(13.dp)
            )
            .padding(13.dp)
            .clickable { onClick() }
            .semantics {
                role = Role.Button
                contentDescription = semanticsContentDescription
            },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    combinedMin: CombinedMinutes,
    onCancel: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    val timePickerState = rememberTimePickerState(combinedMin.hour, combinedMin.minute)

    SelectDialog(
        title = stringResource(id = R.string.alarm_panel_time_picker_title),
        onCancel = { onCancel() },
        onConfirm = {
            onConfirm(timePickerState.hour, timePickerState.minute)
            onCancel()
        },
        content = { androidx.compose.material3.TimePicker(state = timePickerState) }
    )
}

@Composable
fun WeeklyRepeatSection(
    weeklyRepeat: WeeklyRepeat,
    onClick: () -> Unit,
) {
    AlarmPanelSectionRow(title = stringResource(id = R.string.alarm_panel_weeklyrepeat_session_title)){
        Row(
            modifier = Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = when {
                    weeklyRepeat.isRepeating -> getLocalizedShortWeekDayFormatted(weeklyRepeat)
                    else -> stringResource(id = R.string.never)
                },
                style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.End),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                modifier = Modifier.clickable { onClick() },
                imageVector = SiaIcons.ArrowRight,
                contentDescription = stringResource(id = R.string.alarm_panel_weeklyrepeat_picker_open_icon_description),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun WeeklyRepeatPicker(
    weeklyRepeat: WeeklyRepeat,
    onCancel: () -> Unit,
    onConfirm: (Set<Int>) -> Unit,
) {
    val dialogWidth = LocalConfiguration.current.screenWidthDp.dp - 133.dp
    val fullWeekdayNames =  getLocalizedFullWeekdaysMap()
    val selected = remember { mutableStateListOf<Int>() }
    weeklyRepeat.weekdays.forEach { selected.add(it) }

    SelectDialog(
        title = stringResource(id = R.string.alarm_panel_weeklyrepeat_picker_title),
        onCancel = onCancel,
        onConfirm = {
            onConfirm(selected.toSet())
            onCancel()
        },
    ) {
        Column(
            modifier = Modifier
                .width(dialogWidth)
                .padding(13.dp)
        ) {
            for (dayCode in fullWeekdayNames.keys.toList()) {
                key(dayCode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = dayCode in selected,
                                onValueChange = { isChecked ->
                                    if (dayCode !in selected && isChecked) selected.add(dayCode)
                                    else selected.remove(dayCode)
                                },
                                role = Role.Checkbox
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "${stringResource(R.string.every)} ${fullWeekdayNames[dayCode]}")
                        Spacer(modifier = Modifier.width(18.dp))
                        Checkbox(
                            checked = dayCode in selected,
                            onCheckedChange = { isChecked ->
                                if (dayCode !in selected && isChecked) selected.add(dayCode)
                                else selected.remove(dayCode)
                            }
                        )
                    }
                }
                Divider()
            }
        }
        Spacer(modifier = Modifier.height(13.dp))
    }
}

@Composable
fun LabelSection(
    label: String,
    onClick: (String) -> Unit,
    keyboardFocusManager: FocusManager,
) {
    AlarmPanelSectionRow(title = stringResource(id = R.string.alarm_panel_label_session_title)) {
        BasicTextField(
            value = label,
            onValueChange = onClick,
            textStyle = MaterialTheme.typography.labelLarge.copy(
                textAlign = TextAlign.End,
                color = LocalContentColor.current,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardFocusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 13.dp),
            decorationBox = { innerTextField ->
                if (label.isEmpty()) {
                    /* Show hint text. */
                    Text(
                        text = stringResource(id = R.string.alarm_panel_label_section_textfield_placeholder),
                        style = MaterialTheme.typography.labelMedium.copy(
                            textAlign = TextAlign.End,
                            color = Color.Gray,
                        )
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun RingtoneSection(
    ringtone: Uri,
    onClick: () -> Unit,
) {
    val context = LocalContext.current

    AlarmPanelSectionRow(title = stringResource(id = R.string.alarm_panel_ringtone_section_title)) {
        Row(
            modifier = Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // To generate a preview.
            if (ringtone != Uri.EMPTY) {
                val title = RingtoneManager
                    .getRingtone(context, ringtone)
                    .getTitle(context)

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.End),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = SiaIcons.ArrowRight,
                contentDescription = stringResource(id = R.string.alarm_panel_ringtone_picker_open_icon_description),
                modifier = Modifier.clickable { onClick() },
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun RingtonePicker(
    ringtone: Uri,
    onCancel: () -> Unit,
    onConfirm: (Uri) -> Unit,
) {
    val ringtonePickerLauncher =
        rememberLauncherForActivityResult(
            contract =  ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data
                            ?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
                    } else {
                        result.data
                            ?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    }

                    uri?.let (onConfirm)
                }
                onCancel()
            }
        )

    val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        .apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtone)
        }

    SideEffect {
        ringtonePickerLauncher.launch(intent)
    }
}

@Composable
fun SayItTextSection(
    text: List<String>,
    onClick: (List<String>) -> Unit,
    keyboardFocusManager: FocusManager,
) {
    val textHolder = remember { text.toMutableList() }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(13.dp)
            )
            .padding(top = 13.dp, start = 13.dp, end = 13.dp, bottom = 19.dp),
    ) {
        SayItTextInfoExtendableText()
        Spacer(modifier = Modifier.height(8.dp))

        textHolder.indices.forEach { idx ->
            key(idx) {
                SayItTextField(
                    lineNumber = idx + 1,
                    text = textHolder[idx],
                    keyboardFocusManager = keyboardFocusManager,
                    keyboardFocusRequester = focusRequester,
                    onChanged = {
                        // Checks for the index out-of-range error because this function is called after
                        // onDeleteText(), which has already deleted the index (idx) in the textHolder list.
                        if (textHolder.lastIndex >= idx) { textHolder[idx] = it }
                        onClick(textHolder.toList())
                    },
                    onDeleteText = {
                        textHolder.removeAt(idx)
                        onClick(textHolder.toList())
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(13.dp))
        SayItTextAddNewTextButton(
            onClick = {
                textHolder.add("")
                onClick(textHolder.toList())
            },
        )
    }
}

@Composable
fun SayItTextInfoExtendableText() {
    var showFullText by rememberSaveable { mutableStateOf(false) }

    Column(
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.alarm_panel_say_it_section_title),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                onClick = { showFullText = true },
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                ),
            ) {
                Icon(
                    imageVector = SiaIcons.Info,
                    contentDescription = stringResource(id = R.string.alarm_panel_sayit_section_show_info_icon_description),
                )
            }
        }

        if (showFullText) {
            Spacer(modifier = Modifier.height(9.dp))
            Column() {
                Text(
                    text = stringResource(id = R.string.alarm_panel_sayit_text_long_information),
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
                )
                IconButton(
                    onClick = { showFullText = false },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent,
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = SiaIcons.ArrowUp,
                        contentDescription = stringResource(id = R.string.alarm_panel_sayit_section_hide_info_icon_description),
                    )
                }
            }
        }
    }
}

@Composable
fun SayItTextField(
    lineNumber: Int,
    text: String,
    keyboardFocusManager: FocusManager,
    keyboardFocusRequester: FocusRequester,
    onChanged: (String) -> Unit,
    onDeleteText: () -> Unit,
) {
    var textHolder by rememberSaveable { mutableStateOf(text) }

    // Request keyboard focus automatically when user clicked add new text.
    LaunchedEffect(Unit) {
        if (lineNumber > 1 && text == "") keyboardFocusRequester.requestFocus()
    }

    TextField(
        value = textHolder,
        onValueChange = { textHolder = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .padding(bottom = 8.dp)
            // When focus is released call updateSayItText from AddViewModel to save the changes.
            .onFocusChanged { if (!it.isFocused) onChanged(textHolder) }
            .focusRequester(keyboardFocusRequester),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontStyle = FontStyle.Italic,
            color = LocalContentColor.current,
        ),
        maxLines = 13,  // TODO: Dev mode assumption in the future need to set right number.
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardFocusManager.clearFocus() }),
        suffix = {
            if (textHolder.isNotEmpty() && lineNumber != 1) {
                Box(
                    modifier = Modifier
                        .height(23.dp)
                        .width(23.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable { onDeleteText() }
                ) {
                    Icon(
                        imageVector = SiaIcons.Close,
                        contentDescription = stringResource(id = R.string.alarm_panel_sayit_section_textfield_delete_button_description), // TODO
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(0.dp),
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.alarm_panel_sayit_section_textfield_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        }
    )
}

@Composable
fun SayItTextAddNewTextButton(
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = SiaIcons.AddCircle,
            contentDescription = stringResource(id = R.string.alarm_panel_sayit_section_add_new_button_description),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(id = R.string.alarm_panel_sayit_section_add_new_button_text),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Justify,
        )
    }
}

@Composable
fun AlarmPanelSectionRow(
    title: String,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        AlarmPanelSectionTitle(text = title)
        content()
    }
}

@Composable
fun AlarmPanelSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(end = 13.dp),
    )
}

@Composable
fun SelectDialog(
    title: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onCancel) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(13.dp))
                    Button(onClick = { onConfirm(); onCancel() }) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            }
        }
    }
}