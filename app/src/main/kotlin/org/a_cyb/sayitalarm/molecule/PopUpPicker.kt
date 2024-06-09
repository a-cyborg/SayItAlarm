/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.atom.DialogStandardFitContent
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.SpacerXLarge

@Composable
fun <T> PopUpPickerStandardWheel(
    title: String,
    info: String = "",
    pickerValues: List<T>,
    pickerInitIdx: Int = 0,
    pickerItemRow: @Composable (T) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (idx: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()

    DialogStandardFitContent(onDismiss = onDismiss) {
        SpacerXLarge()
        TextRowTitleAndInfo(title = title, info = info)
        SpacerMedium()
        Box {
            WheelPicker(
                values = pickerValues,
                initIdx = pickerInitIdx,
                itemRow = { pickerItemRow(it) },
                onCancel = onDismiss,
                onConfirm = {
                    scope.launch { onConfirm(it) }
                        .invokeOnCompletion { onDismiss() }
                },
            )
        }
        SpacerXLarge()
    }
}
