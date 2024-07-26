/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.design_system.atom.TextHeadlineStandardLarge

@Preview
@Composable
fun WheelPickerPreview() {
    val colors: List<String> = listOf(
        "Red", "Blue", "Green", "Yellow", "Purple",
        "Orange", "Pink", "Brown", "Black", "White",
        "Gray", "Turquoise", "Maroon",
    )

    WheelPicker(
        values = colors,
        initIdx = 4,
        itemRow = { TextHeadlineStandardLarge(text = it) },
        onCancel = {},
        onConfirm = { _ -> },
    )
}
