package org.a_cyb.sayitalarm.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.designsystem.component.SiaIcons
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme
import org.a_cyb.sayitalarm.core.model.CombinedMinutes
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat
import org.a_cyb.sayitalarm.feature.list.ListUiState.Error
import org.a_cyb.sayitalarm.feature.list.ListUiState.Loading
import org.a_cyb.sayitalarm.feature.list.ListUiState.Success
import org.a_cyb.sayitalarm.util.AlarmListItemPreviewParameterProvider
import org.a_cyb.sayitalarm.util.getLocalizedShortWeekDayFormatted
import org.a_cyb.sayitalarm.util.getFormattedClockTime

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    onEditMode: Boolean,
    offEditMode: () -> Unit,
    navigateToEdit: (Int) -> Unit,
) {
    val listUiState by viewModel.listUiState.collectAsStateWithLifecycle()

    // Turn off edit mode when the list screen is recomposed.
    LaunchedEffect(Unit) { offEditMode() }

    ListScreen(
        listUiState = listUiState,
        onEditMode = onEditMode,
        onAlarmEnabledStateChange = viewModel::updateAlarmEnabledState,
        onDeleteAlarm = viewModel::deleteAlarm,
        navigateToEdit = navigateToEdit,
    )
}

@Composable
fun ListScreen(
    listUiState: ListUiState,
    onEditMode: Boolean,
    onAlarmEnabledStateChange: (Int, Boolean) -> Unit,
    onDeleteAlarm: (Int) -> Unit,
    navigateToEdit: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTag = "ListScreen" }
    ) {
        when (listUiState) {
            is Loading -> {
                Text(
                    text = stringResource(id = R.string.loading),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is Success -> {
                AlarmListColumn(
                    editMode = onEditMode,
                    alarms = listUiState.alarms,
                    onAlarmEnabledStateChange = onAlarmEnabledStateChange,
                    navigateToEdit = navigateToEdit,
                    onDeleteAlarm = onDeleteAlarm,
                )
            }
            Error -> {}
        }
    }
}

@Composable
private fun AlarmListColumn(
    editMode: Boolean,
    alarms: List<AlarmListItem>,
    onAlarmEnabledStateChange: (Int, Boolean) -> Unit,
    onDeleteAlarm: (Int) -> Unit,
    navigateToEdit: (Int) -> Unit,
) {
    LazyColumn(Modifier.padding(8.dp)) {
        item {
            Text(
                text = stringResource(id = R.string.alarms),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(12.dp)
            )
            Divider(Modifier.height(3.dp))
            Spacer(Modifier.height(6.dp))
        }
        items(alarms) { alarm ->
            AlarmListItem(
                editMode = editMode,
                alarmTime = alarm.time,
                alarmLabel = alarm.label,
                alarmWeeklyRepeat = alarm.weeklyRepeat,
                alarmEnabledState = alarm.enabled,
                onAlarmEnabledStateChange = { enabled -> onAlarmEnabledStateChange(alarm.id, enabled) },
                navigateToEdit = { navigateToEdit(alarm.id) },
                onDeleteAlarm = { onDeleteAlarm(alarm.id) },
            )
       }
    }
}

@Composable
private fun AlarmListItem(
    editMode: Boolean,
    alarmTime: CombinedMinutes,
    alarmLabel: String,
    alarmWeeklyRepeat: WeeklyRepeat,
    alarmEnabledState: Boolean,
    onAlarmEnabledStateChange: (Boolean) -> Unit,
    onDeleteAlarm: () -> Unit,
    navigateToEdit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 3.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Display delete button.
        if (editMode) {
            IconButton(
                onClick = { onDeleteAlarm() },
                modifier = Modifier.fillMaxHeight(),
                colors = IconButtonDefaults.filledIconButtonColors(
                    contentColor = Color.Red,
                    containerColor = Color.Transparent,
                )
            ) {
                Icon(
                    imageVector = SiaIcons.Delete,
                    contentDescription = stringResource(id = R.string.list_delete_icon_description),
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .weight(1f)
                .padding(13.dp),
        ) {
            Text(
                text = getFormattedClockTime(alarmTime),
                style = MaterialTheme.typography.titleLarge
            )

            val labelAndRepeatText = when(alarmLabel.isEmpty()) {
                true -> getLocalizedShortWeekDayFormatted(alarmWeeklyRepeat)
                false -> {
                    if (alarmWeeklyRepeat.isRepeating) {
                        "$alarmLabel, ${getLocalizedShortWeekDayFormatted(alarmWeeklyRepeat)}"
                    } else {
                        alarmLabel
                    }
                }
            }

            Text(
                text = labelAndRepeatText,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Box(modifier = Modifier.padding(end = 13.dp)) {
            when (editMode) {
                true -> IconButton(onClick = { navigateToEdit() }) {
                    Icon(
                        imageVector = SiaIcons.ArrowRight,
                        contentDescription = stringResource(id = R.string.list_edit_icon_description),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                false -> Switch(
                    checked = alarmEnabledState,
                    onCheckedChange = { onAlarmEnabledStateChange(it) },
                    modifier = Modifier.semantics { testTag = "Switch$alarmLabel" }
                )
            }
        }
    }
    Divider()
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmListScreenSuccess(
    @PreviewParameter(AlarmListItemPreviewParameterProvider::class) alarmItems: List<AlarmListItem>
) {
    SayItAlarmTheme {
        ListScreen(
            listUiState = Success(alarmItems),
            onEditMode = false,
            onAlarmEnabledStateChange = { _, _ -> },
            onDeleteAlarm = { _ -> },
            navigateToEdit = { _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmListScreenSuccessEditMode(
    @PreviewParameter(AlarmListItemPreviewParameterProvider::class) alarmItems: List<AlarmListItem>
) {
    SayItAlarmTheme {
        ListScreen(
            listUiState = Success(alarmItems),
            onEditMode = true,
            onAlarmEnabledStateChange = { _, _ -> },
            onDeleteAlarm = { _ -> },
            navigateToEdit = { _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlamListScreenLoading() {
    SayItAlarmTheme {
        ListScreen(
            listUiState = Loading,
            onEditMode = true,
            onAlarmEnabledStateChange = { _, _ -> },
            onDeleteAlarm = { _ -> },
            navigateToEdit = { _ -> },
        )
    }
}