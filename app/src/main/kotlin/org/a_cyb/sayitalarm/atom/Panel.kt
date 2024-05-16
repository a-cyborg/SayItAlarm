/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.token.Color
import org.a_cyb.sayitalarm.token.Shape
import org.a_cyb.sayitalarm.token.Spacing

@Composable
fun PanelStandard(
    panelItems: List<@Composable () -> Unit>,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = Color.surface.standard, shape = Shape.PanelSection.primary)
            .padding(horizontal = Spacing.m),
    ) {
        panelItems.forEach { item ->
            item()
            DividerStandard()
        }
    }
}
