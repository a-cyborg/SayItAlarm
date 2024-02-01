package org.a_cyb.sayitalarm.feature.edit

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.designsystem.component.AlarmPanel
import org.a_cyb.sayitalarm.core.designsystem.component.SiaDialog
import org.a_cyb.sayitalarm.core.designsystem.component.SiaTopAppBarIconButton
import org.a_cyb.sayitalarm.util.getRandomAlarm

@Composable
fun EditDialog(
    viewModel: EditViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val editUiState by viewModel.editUiState.collectAsStateWithLifecycle()

    EditDialog(
        editUiState = editUiState,
        onDismiss = onDismiss,
        onUpdateTime = viewModel::onUpdateCombinedMinutes,
        onUpdateWeeklyRepeat = viewModel::onUpdateWeeklyRepeat,
        onUpdateLabel = viewModel::onUpdateLabel,
        onUpdateRingtone = viewModel::onUpdateRingtone,
        onUpdateSayItText = viewModel::onUpdateSayItText,
        onDelete = viewModel::onDeleteAlarm,
        onSave = viewModel::onSave,
        onCancel = viewModel::onCancel,
    )
}

@Composable
fun EditDialog(
    onDismiss: () -> Unit,
    editUiState: EditUiState,
    onUpdateTime: (Int, Int) -> Unit,
    onUpdateWeeklyRepeat: (Set<Int>) -> Unit,
    onUpdateLabel: (String) -> Unit,
    onUpdateRingtone: (Uri) -> Unit,
    onUpdateSayItText: (List<String>) -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    SiaDialog(
        // Here when user clicks cancel button set the alarm with original value.
        onDismiss = { onCancel(); onDismiss() },
        topAppBarTitleRes = R.string.edit_topbar_title,
        topAppBarFirstActionItem = {
            SiaTopAppBarIconButton(
                onClick = { onSave(); onDismiss() },
                buttonTextResId = R.string.save
            )
        }
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            when (editUiState) {
                is EditUiState.Loading -> {
                    Text(
                        text = stringResource(id = R.string.loading),
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is EditUiState.Success -> {
                    EditPanel(
                        editUiState = editUiState,
                        onUpdateTime = onUpdateTime,
                        onUpdateWeeklyRepeat = onUpdateWeeklyRepeat,
                        onUpdateLabel = onUpdateLabel,
                        onUpdateRingtone = onUpdateRingtone,
                        onUpdateSayItText = onUpdateSayItText,
                        onDeleteAlarm = onDelete,
                        onDismiss = onDismiss,
                    )
                }
                is EditUiState.Error -> { /* TODO: Show snackbar and dismiss dialog. */}
            }
        }
    }
}

@Composable
fun EditPanel(
    editUiState: EditUiState.Success,
    onUpdateTime: (Int, Int) -> Unit,
    onUpdateWeeklyRepeat: (Set<Int>) -> Unit,
    onUpdateLabel: (String) -> Unit,
    onUpdateRingtone: (Uri) -> Unit,
    onUpdateSayItText: (List<String>) -> Unit,
    onDeleteAlarm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val editedAlarm by editUiState.editedAlarm.collectAsStateWithLifecycle()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(13.dp))
        AlarmPanel(
            alarm = editedAlarm,
            onUpdateAlarmTime = onUpdateTime,
            onUpdateWeeklyRepeat = onUpdateWeeklyRepeat,
            onUpdateLabel = onUpdateLabel,
            onUpdateRingtone = onUpdateRingtone,
            onUpdateSayItText = onUpdateSayItText,
        )
        Spacer(modifier = Modifier.height(8.dp))
        IconButton(
            onClick = { onDeleteAlarm(); onDismiss() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSecondary,
                    shape = RoundedCornerShape(13.dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.delete_alarm),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = Color.Red,
            )
        }
        Spacer(modifier = Modifier.height(23.dp))
    }
}

@Preview
@Composable
fun EditDialogSuccessPreview() {
    val fetchedAlarm = getRandomAlarm()
    EditDialog(
        onDismiss = { -> },
        editUiState = EditUiState.Success(fetchedAlarm, MutableStateFlow(fetchedAlarm)),
        onUpdateTime = { _, _ -> },
        onUpdateWeeklyRepeat = { _ -> },
        onUpdateLabel = { _ -> },
        onUpdateRingtone = { _ -> },
        onUpdateSayItText = { _ -> },
        onDelete = { -> },
        onSave = { -> },
        onCancel = { -> },
    )
}

@Preview
@Composable
fun EditDialogLoadingPreview() {
    EditDialog(
        onDismiss = { -> },
        editUiState = EditUiState.Loading,
        onUpdateTime = { _, _ -> },
        onUpdateWeeklyRepeat = { _ -> },
        onUpdateLabel = { _ -> },
        onUpdateRingtone = { _ -> },
        onUpdateSayItText = { _ -> },
        onDelete = { -> },
        onSave = { -> },
        onCancel = { -> },
    )
}
