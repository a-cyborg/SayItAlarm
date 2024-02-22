package org.a_cyb.sayitalarm.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.core.designsystem.theme.SayItAlarmTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiaTopAppBar(
    @StringRes titleResId: Int? = null,
    navigationItem: @Composable () -> Unit,
    firstActionItem: (@Composable () -> Unit)? = null,
    secondActionItem: (@Composable () -> Unit)? = null,
    ) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (titleResId != null) stringResource(id = titleResId) else "",
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = { navigationItem() },
        actions = {
            firstActionItem?.invoke()
            secondActionItem?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}

/**
 * Say it Alarm IconButton with icon content. Wraps Material3 [IconButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param icon The button icon content.
 * @param contentDescriptionRes text used by accessibility services to describe what this icon represents.
 * @param enabled Controls the enabled state of the button.
 */
@Composable
fun SiaTopAppBarIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    @StringRes contentDescriptionRes: Int,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = { onClick() },
        enabled = enabled,
        colors = IconButtonDefaults.outlinedIconButtonColors()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = contentDescriptionRes),
        )
    }
}

/**
 * Say it Alarm IconButton with text content. Wraps Material3 [IconButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param buttonTextResId The button text content.
 * @param enabled Controls the enabled state of the button.
 */
@Composable
fun SiaTopAppBarIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes buttonTextResId: Int,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = { onClick() },
        enabled = enabled,
        modifier = modifier.width(58.dp)
    ) {
        Text(
            text = stringResource(id = buttonTextResId),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview
@Composable
fun PreviewTaTopAppBar() {
    SayItAlarmTheme {
        SiaTopAppBar(
            titleResId = R.string.app_name,
            navigationItem = {
                SiaTopAppBarIconButton(
                    onClick = { },
                    buttonTextResId = R.string.sia_app_topbar_edit_icon_text
                )
            },
            firstActionItem = {
                SiaTopAppBarIconButton(
                    onClick = {},
                    icon = SiaIcons.Add,
                    contentDescriptionRes = R.string.sia_app_topbar_add_icon_description
                )
            },
            secondActionItem = {
                SiaTopAppBarIconButton(
                    onClick = {},
                    icon = SiaIcons.Settings,
                    contentDescriptionRes = R.string.sia_app_topbar_settings_icon_description
                )
            }
        )
    }
}