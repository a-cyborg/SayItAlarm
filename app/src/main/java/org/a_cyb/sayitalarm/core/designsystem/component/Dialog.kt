package org.a_cyb.sayitalarm.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme

@Composable
fun SiaDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    topAppBarTitleRes: Int? = null,
    topAppBarFirstActionItem: (@Composable () -> Unit)? = null,
    topAppBarSecondActionItem: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, top = 23.dp, end = 18.dp, bottom = 13.dp)
                .then(modifier),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        ) {
            SiaTopAppBar(
                titleResId = topAppBarTitleRes,
                navigationItem = {
                    SiaTopAppBarIconButton(
                        onClick = { onDismiss() },
                        icon = SiaIcons.Close,
                        contentDescriptionRes = R.string.topbar_dialog_close_icon_description
                    )
                },
                firstActionItem = topAppBarFirstActionItem,
                secondActionItem = topAppBarSecondActionItem,
            )

            Box(modifier = modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTaDialog() {
    SayItAlarmTheme {
        SiaDialog(onDismiss = { }) {}
    }
}