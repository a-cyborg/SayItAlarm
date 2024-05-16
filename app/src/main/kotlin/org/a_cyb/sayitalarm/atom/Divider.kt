/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.token.Color
import org.a_cyb.sayitalarm.token.Spacing

@Composable
fun DividerStandard() {
    Divider(
        color = Color.text.subtle.copy(alpha = 0.3f),
        thickness = 0.8.dp,
        modifier = Modifier.padding(horizontal = Spacing.xs),
    )
}
