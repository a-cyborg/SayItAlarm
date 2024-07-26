/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import org.a_cyb.sayitalarm.design_system.screen.SettingsViewModelFake
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.IconButtonEdit

@Preview
@Composable
fun PanelRowStandardWithoutValuePreview() {
    PanelItemStandard(
        valueLabel = stringResource(id = R.string.about),
        afterContent = { IconButtonEdit {} },
    )
}

@Preview
@Composable
fun PanelRowStandardWithoutAfterContentPreview() {
    PanelItemStandard(
        valueLabel = stringResource(id = R.string.version),
        value = "1.0",
    )
}

@Preview
@Composable
fun PanelItemPreviewWithPopUpPickerStandardWheelPreview() {
    val fakeViewModel = SettingsViewModelFake()

    PanelItemWithPopupPickerStandardWheel(
        title = stringResource(id = R.string.timeout),
        info = stringResource(id = R.string.info_timeout),
        values = fakeViewModel.timeOuts.map { it.formatted },
        selectedItemIdx = 150,
        popUpPickerOnConfirm = { _ -> }
    )
}