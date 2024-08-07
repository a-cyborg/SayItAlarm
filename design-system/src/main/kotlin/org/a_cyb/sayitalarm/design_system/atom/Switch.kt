/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import org.a_cyb.sayitalarm.design_system.token.Color

@Composable
fun SwitchStandard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.surface.attention,
            checkedTrackColor = Color.surface.standard,
            checkedBorderColor = Color.surface.attention,
            uncheckedThumbColor = Color.surface.strong,
            uncheckedTrackColor = Color.surface.standard,
        ),
    )
}
