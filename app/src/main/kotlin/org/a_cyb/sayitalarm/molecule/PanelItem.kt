/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.TextBodyStandardLarge
import org.a_cyb.sayitalarm.atom.TextBodySubtleMedium
import org.a_cyb.sayitalarm.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.token.Color
import org.a_cyb.sayitalarm.token.Shape
import org.a_cyb.sayitalarm.token.Sizing

@Composable
fun PanelItemStandard(
    valueLabel: String,
    value: String = "",
    afterContent: @Composable () -> Unit = { SpacerMedium() },
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = Sizing.PanelRow.MinHeight),
    ) {
        TextBodyStandardLarge(text = valueLabel)
        Spacer(Modifier.weight(1f))
        TextBodySubtleMedium(text = value)
        afterContent()
    }
}

@Composable
fun PanelItemStandardClickable(
    valueLabel: String = "",
    value: String = "",
    onClick: (String) -> Unit,
) {
    val description = stringResource(id = R.string.action_edit)

    Row(
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = Sizing.PanelRow.MinHeight)
            .clickable { onClick(value) }
            .semantics { contentDescription = description },
    ) {
        if (valueLabel == "") SpacerMedium() else TextBodyStandardLarge(text = valueLabel)
        TextBodySubtleMedium(text = value)
    }
}

@Composable
fun PanelItemClickableBordered(
    contentDescription: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = contentDescription,
                onClick = onClick,
                role = Role.Button,
            )
            .border(BorderStroke(1.dp, Color.text.standard), Shape.Panel.primary),
    ) {
        content()
    }
}

@Composable
fun PanelItemWithPopupPickerStandardWheel(
    title: String,
    info: String = "",
    values: List<String>,
    pickerItemRow: @Composable (String) -> Unit = @Composable { TextDisplayStandardSmall(text = it) },
    selectedItemIdx: Int,
    popUpPickerOnConfirm: (idx: Int) -> Unit,
) {
    var showPopUpPicker by remember { mutableStateOf(false) }

    PanelItemStandard(
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
            pickerItemRow = pickerItemRow,
            onCancel = { showPopUpPicker = false },
            onConfirm = { popUpPickerOnConfirm(it) },
        )
    }
}

@Composable
fun PanelItemWithPopupPicker(
    valueLabel: String,
    value: String,
    popupPicker: @Composable (() -> Unit) -> Unit,
) {
    var showPopUpPicker by rememberSaveable { mutableStateOf(false) }

    PanelItemStandard(
        valueLabel = valueLabel,
        value = value,
    ) {
        IconButtonEdit { showPopUpPicker = true }
    }

    if (showPopUpPicker) {
        popupPicker { showPopUpPicker = false }
    }
}
