/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.molecule.animateCircleBorder
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.design_system.token.Icon

@Composable
fun IconButtonAdd(
    contentDescription: String = stringResource(id = R.string.action_add_alarm),
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.add,
            tint = Color.text.attention,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun IconButtonClose(
    contentDescription: String = stringResource(id = R.string.action_close),
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.close,
            tint = Color.text.attention,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun IconButtonDelete(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.delete,
            tint = Color.text.danger,
            contentDescription = stringResource(id = R.string.action_delete_alarm),
        )
    }
}

@Composable
fun IconButtonDeleteText(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleDangerMedium(text = stringResource(id = R.string.delete))
    }
}

@Composable
fun IconButtonDone(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.done))
    }
}

@Composable
fun IconButtonEdit(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.arrowRight,
            tint = Color.text.attention,
            contentDescription = stringResource(id = R.string.action_edit),
        )
    }
}

@Composable
fun IconButtonEditText(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.edit))
    }
}

@Composable
fun IconButtonSettings(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.settings,
            tint = Color.text.attention,
            contentDescription = stringResource(id = R.string.action_open_settings),
        )
    }
}

@Composable
fun IconButtonNavigateBack(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.arrowLeft,
            tint = Color.text.attention,
            contentDescription = stringResource(id = R.string.action_navigate_back),
        )
    }
}

@Composable
fun IconButtonInfo(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.info,
            tint = Color.text.attention,
            contentDescription = stringResource(id = R.string.action_info),
        )
    }
}

@Composable
fun IconButtonCollapse(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icon.arrowUp,
            tint = Color.text.attention,
            contentDescription = stringResource(id = R.string.action_collapse),
        )
    }
}

@Composable
fun IconButtonSaveText(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.save))
    }
}

@Composable
fun IconButtonCancelText(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleWarningMedium(text = stringResource(id = R.string.cancel))
    }
}

@Composable
fun IconButtonConfirmText(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.confirm))
    }
}

@Composable
fun IconButtonSnoozeText(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.snooze))
    }
}

@Composable
fun IconButtonRequestPermission(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.request_permission))
    }
}

@Composable
fun IconButtonSayIt(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .size(160.dp)
            .animateCircleBorder()
    ) {
        TextTitleAttentionLarge(
            text = stringResource(id = R.string.say_it)
        )
    }
}
