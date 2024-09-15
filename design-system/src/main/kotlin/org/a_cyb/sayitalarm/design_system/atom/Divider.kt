/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.design_system.token.Spacing

@Composable
fun DividerStandard() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Spacing.xs),
        thickness = 0.8.dp,
        color = Color.text.subtle.copy(alpha = 0.3f),
    )
}

@Composable
fun DividerMedium() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Spacing.l),
        thickness = 0.5.dp,
        color = Color.text.subtle.copy(alpha = 0.3f),
    )
}
