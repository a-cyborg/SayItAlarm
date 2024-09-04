/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.design_system.organism.FakeAlarmUIData

@Preview
@Composable
fun PopUpPickerStandardWheelPreview() {
    val colors: List<String> = listOf(
        "Red", "Blue", "Green", "Yellow", "Purple",
        "Orange", "Pink", "Brown", "Black", "White",
        "Gray", "Turquoise", "Maroon",
    )

    PopUpPickerStandardWheel(
        title = "Colors",
        info = "My favorite color is",
        pickerValues = colors,
        pickerInitIdx = 6,
        pickerItemRow = { TextDisplayStandardSmall(text = it) },
        onCancel = {},
        onConfirm = { _ -> },
    )
}

@Preview
@Composable
fun PopupPickerTimePreview() {
    PopupPickerTime(
        hour = 8,
        minute = 30,
        onConfirm = { _, _ -> },
        onCancel = {}
    )
}

@Preview
@Composable
fun PopupPickerRepeatPreview() {
    val selectableRepeats = FakeAlarmUIData.defaultSelectableRepeats

    PopupPickerRepeat(
        title = stringResource(id = R.string.repeat),
        selectableRepeats = selectableRepeats,
        onConfirm = { _ -> },
        onCancel = {}
    )
}

@Preview
@Composable
fun PopupPickerLabelPreview() {
    PopupPickerLabel(
        label = "Label",
        onConfirm = { _ -> },
        onCancel = {},
    )
}

@Preview
@Composable
fun PopupPickerSayItScriptPreview() {
    PopupPickerSayItScript(
        script = "Test",
        onConfirm = { _ -> },
        onCancel = {},
        onDelete = {}
    )
}
