/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.design_system.token.Icon

@Composable
fun IconButtonAdd(
    contentDescription: String = stringResource(id = R.string.action_add_alarm),
    onClick: () -> Unit,
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
