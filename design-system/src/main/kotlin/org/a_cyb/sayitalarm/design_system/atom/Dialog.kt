/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.a_cyb.sayitalarm.design_system.token.Color

@Composable
fun DialogStandardFillMax(
    onDismiss: () -> Unit,
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.surface.standard)
        ) {
            CardStandardFillMax {
                topAppBar()
                content()
                SpacerXLarge()
            }
        }
    }
}

@Composable
fun DialogStandardFillMaxScrollable(
    onDismiss: () -> Unit,
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.surface.standard)
        ) {
            ColumnScreenStandardScrollable {
                topAppBar()
                content()
                SpacerXLarge()
            }
        }
    }
}

@Composable
fun DialogStandardFitContent(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        CardStandardCentered {
            SpacerXLarge()
            content()
            SpacerXLarge()
        }
    }
}

@Composable
fun DialogStandardFitContentScrollable(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        CardStandardCenteredScrollable {
            SpacerXLarge()
            content()
            SpacerXLarge()
        }
    }
}
