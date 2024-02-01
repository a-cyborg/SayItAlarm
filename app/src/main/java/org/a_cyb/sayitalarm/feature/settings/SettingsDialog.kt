package org.a_cyb.sayitalarm.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.designsystem.component.SiaDialog
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme
import org.a_cyb.sayitalarm.core.model.AlarmVolumeButtonBehavior
import org.a_cyb.sayitalarm.core.model.DarkThemeConfig
import org.a_cyb.sayitalarm.core.model.UserData

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {

    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsDialog(
        onDismiss = onDismiss,
        settingsUiState = settingsUiState,
        onChangeAlarmTimeOut = viewModel::updateAlarmTimeOut,
        onChangeSnoozeLength = viewModel::updateSnoozeLength,
        onChangeAlarmCrescendoDuration = viewModel::updateAlarmCrescendoDuration,
        onChangeAlarmVolumeButtonBehavior = viewModel::updateAlarmVolumeButtonBehavior,
        onChangeDarkThemConfig = viewModel::updateDarkThemConfig,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    settingsUiState: SettingsUiState,
    onChangeAlarmTimeOut: (min: Int) -> Unit,
    onChangeSnoozeLength: (min: Int) -> Unit,
    onChangeAlarmCrescendoDuration: (min: Int) -> Unit,
    onChangeAlarmVolumeButtonBehavior: (volumeButtonBehavior: AlarmVolumeButtonBehavior) -> Unit,
    onChangeDarkThemConfig: (darkTheme: DarkThemeConfig) -> Unit,
) {
    SiaDialog(
        onDismiss = { onDismiss() },
        topAppBarTitleRes = R.string.settings_topbar_title,
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            when(settingsUiState) {
                is SettingsUiState.Loading -> { }
                is SettingsUiState.Success ->
                    SettingsPanel(
                        userData = settingsUiState.userData,
                        onChangeAlarmTimeOut = onChangeAlarmTimeOut,
                    )
            }
        }
    }
}

@Composable
private fun SettingsPanel(
    userData: UserData,
    onChangeAlarmTimeOut: (min: Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(12.dp)
            .background(
                color = MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(13.dp)
            )
            .padding(top = 13.dp, start = 13.dp, end = 13.dp, bottom = 19.dp)
    ) {
        PanelSectionTitle(text = stringResource(id = R.string.open_source_licenses))

        // TODO: Add about SayIt Alarm section
    }
}

@Composable
fun PanelSectionRow(
    title: String,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        PanelSectionTitle(text = title)
        content()
    }
}

@Composable
fun PanelSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(end = 13.dp),
    )
}

@Preview
@Composable
private fun PreviewSettingsDialog() {
    val fakeUserData = UserData(
        timeOut = 30,
        snoozeLength = 3,
        ringtoneCrescendoDuration = 1,
        volumeButtonBehavior = AlarmVolumeButtonBehavior.SNOOZE,
        darkThemeConfig = DarkThemeConfig.DARK,
    )

    SayItAlarmTheme {
        SettingsDialog(
            onDismiss = { },
            settingsUiState = SettingsUiState.Success(fakeUserData),
            onChangeAlarmTimeOut = { _ -> },
            onChangeSnoozeLength = { _ -> },
            onChangeAlarmCrescendoDuration =  { _ -> },
            onChangeAlarmVolumeButtonBehavior = { _ -> },
            onChangeDarkThemConfig = { _ -> }
        )
    }
}

/*
        Row(
//                modifier = Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

        ) {
            PanelSectionTitle(text = stringResource(id = R.string.settings_alarm_timeout_title))

            Text(
                text = userData.timeOut.toString(),
                style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.End),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.min),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Divider(Modifier.padding(vertical = 13.dp))

        Row(
            //                modifier = Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PanelSectionTitle(text = stringResource(id = R.string.settigns_snooze_section_title))

            Text(
                text = userData.timeOut.toString(),
                style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.End),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.min),
                style = MaterialTheme.typography.labelSmall,
            )
        }

//        Text(text = "alarmCrescendo = ${userData.ringtoneCrescendoDuration}")
//        Text(text = "volume behaviour = ${userData.volumeButtonBehavior}")
//        Text(text = "darkThemeConfig = ${userData.darkThemeConfig}")

 */
