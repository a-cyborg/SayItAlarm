/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.token.Color
import org.a_cyb.sayitalarm.token.Shape

@Composable
fun SectionRow(content: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.surface.subtle,
                shape = Shape.PanelSection.primary,
            )
            .padding(13.dp),
    ) {
        content()
    }
}

@Composable
fun SectionRowClickable(
    contentDescription: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.surface.subtle,
                shape = Shape.PanelSection.primary,
            )
            .padding(13.dp)
            .clickable(
                onClickLabel = contentDescription,
                onClick = onClick,
                role = Role.Button,
            ),
    ) {
        content()
    }
}
