package org.a_cyb.sayitalarm.feature.add

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.alarm.NoOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import org.a_cyb.sayitalarm.core.designsystem.component.AlarmPanel
import org.a_cyb.sayitalarm.core.designsystem.component.SiaDialog
import org.a_cyb.sayitalarm.core.designsystem.component.SiaTopAppBarIconButton
import org.a_cyb.sayitalarm.core.model.Alarm
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat

@Composable
fun AddDialog(
    onDismiss: () -> Unit,
    viewModel: AddViewModel = hiltViewModel(),
) {
    val addUiState by viewModel.addUiState.collectAsStateWithLifecycle()
    
    AddDialog(
        addUiState = addUiState,
        onDismiss = { onDismiss() },
        onConfirm = viewModel::saveAlarm,
        onUpdateTime = viewModel::updateTime,
        onUpdateWeeklyRepeat = viewModel::updateWeeklyRepeat,
        onUpdateLabel = viewModel::updateLabel,
        onUpdateRingtone = viewModel::updateRingtone,
        onUpdateSayItText = viewModel::updateSayItText,
    )
}

@Composable
fun AddDialog(
    addUiState: AddUiSate,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onUpdateTime: (Int, Int) -> Unit,
    onUpdateWeeklyRepeat: (Set<Int>) -> Unit,
    onUpdateLabel: (String) -> Unit,
    onUpdateRingtone: (Uri) -> Unit,
    onUpdateSayItText: (List<String>) -> Unit,
) {
    // To prevent multiple clicks on save button.
    // TODO: Impl into a class and wrap with Modifier.
    var lastSaveTimeMs by rememberSaveable { mutableLongStateOf(0L) }

    SiaDialog(
        onDismiss = { onDismiss() },
        topAppBarTitleRes = R.string.add_dialog_topbar_title,
        topAppBarFirstActionItem = {
            SiaTopAppBarIconButton(
                onClick = {
                    // To prevent multiple clicks.
                    if (System.currentTimeMillis() - lastSaveTimeMs >= 300L) {
                        onConfirm();
                    }

                    lastSaveTimeMs = System.currentTimeMillis()

                    onDismiss()
                },
                buttonTextResId = R.string.save
            )
        },
        content = {
            Column(modifier= Modifier.verticalScroll(rememberScrollState())) {
                AlarmPanel(
//                    addUiState = addUiState,
                    alarm = Alarm(
                        combinedMinutes = addUiState.time,
                        label = addUiState.label,
                        weeklyRepeat = addUiState.weeklyRepeat,
                        enabled = true,
                        vibrate = false,    // Not implemented.
                        ringtone = addUiState.ringtone,
                        alarmTerminator = VoiceRecognitionTerminator(addUiState.sayItText),
                        alarmOptionalFeature = NoOptionalFeature,
                    ),
                    onUpdateAlarmTime = onUpdateTime,
                    onUpdateWeeklyRepeat = onUpdateWeeklyRepeat,
                    onUpdateLabel = onUpdateLabel,
                    onUpdateRingtone = onUpdateRingtone,
                    onUpdateSayItText = onUpdateSayItText,
                )
                Spacer(modifier = Modifier.height(33.dp))
            }
        }
    )
}

@Preview
@Composable
fun AddScreenPreview() {
    AddDialog(
        addUiState = AddUiSate(
            time = CombinedMinutes(8, 30),
            weeklyRepeat = WeeklyRepeat.EVERYDAY,
            label = "Label",
            ringtone = Uri.EMPTY,
        ),
        onDismiss = { -> },
        onConfirm = { -> },
        onUpdateTime = { _, _ -> },
        onUpdateWeeklyRepeat = { _ -> },
        onUpdateLabel = { _ -> },
        onUpdateRingtone = { _ -> },
        onUpdateSayItText = { _ -> },
    )
}