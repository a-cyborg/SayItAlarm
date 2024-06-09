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
import org.a_cyb.sayitalarm.screen.SettingsViewModelFake

@Preview
@Composable
fun PanelItemPreviewWithPopUpPickerPreview() {
    val fakeViewModel = SettingsViewModelFake()

    PanelItemWithPopUpPicker(
        title = stringResource(id = R.string.timeout),
        info = stringResource(id = R.string.info_timeout),
        values = fakeViewModel.timeOuts.map { it.formatted },
        selectedItemIdx = 150,
        popUpPickerOnConfirm = { _ -> }
    )
}