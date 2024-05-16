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

@Preview
@Composable
fun PopUpPickerStandardWheelPreview() {
    PopUpPickerStandardWheel(
        title = stringResource(id = R.string.timeout),
        info = stringResource(id = R.string.info_timeout),
        pickerValues = (30..300).toList(),
        pickerItemRow = { TextRowTimeDuration(minutes = it) },
        onDismiss = {},
        onConfirm = { _ -> },
    )
}
