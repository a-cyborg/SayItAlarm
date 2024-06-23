/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.TextDisplayStandardSmall

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
    val selectableRepeat = mapOf(
        "Sunday" to 1, "Monday" to 2, "Tuesday" to 3, "Wednesday" to 4,
        "Thursday" to 5, "Friday" to 6, "Saturday" to 7,
    )

    PopupPickerRepeat(
        title = stringResource(id = R.string.repeat),
        selectedRepeat = emptySet(),
        selectableRepeat = selectableRepeat,
        onConfirm = { _ -> },
        onCancel = {}
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
