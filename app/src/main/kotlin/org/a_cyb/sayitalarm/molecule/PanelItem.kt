/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.atom.PanelRowStandard
import org.a_cyb.sayitalarm.atom.TextDisplayStandardSmall

@Composable
fun PanelItemWithPopUpPicker(
    title: String,
    info: String = "",
    values: List<String>,
    selectedItemIdx: Int,
    popUpPickerOnConfirm: (idx: Int) -> Unit,
) {
    var showPopUpPicker by remember { mutableStateOf(false) }

    PanelRowStandard(
        valueLabel = title,
        value = values[selectedItemIdx],
    ) {
        IconButtonEdit { showPopUpPicker = true }
    }

    if (showPopUpPicker) {
        PopUpPickerStandardWheel(
            title = title,
            info = info,
            pickerValues = values,
            pickerInitIdx = selectedItemIdx,
            pickerItemRow = { TextDisplayStandardSmall(text = it) },
            onDismiss = { showPopUpPicker = false },
            onConfirm = { popUpPickerOnConfirm(it) },
        )
    }
}
