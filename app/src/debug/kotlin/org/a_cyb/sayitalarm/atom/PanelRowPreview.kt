/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.R

@Preview
@Composable
fun PanelRowStandardPreview() {
    PanelRowStandard(
        valueLabel = stringResource(id = R.string.timeout),
        value = stringResource(id = R.string.minute_short, 180),
        afterContent = { IconButtonEdit {} },
    )
}

@Preview
@Composable
fun PanelRowStandardWithoutValuePreview() {
    PanelRowStandard(
        valueLabel = stringResource(id = R.string.about),
        afterContent = { IconButtonEdit {} },
    )
}

@Preview
@Composable
fun PanelRowStandardWithoutAfterContentPreview() {
    PanelRowStandard(
        valueLabel = stringResource(id = R.string.version),
        value = "1.0",
    )
}
