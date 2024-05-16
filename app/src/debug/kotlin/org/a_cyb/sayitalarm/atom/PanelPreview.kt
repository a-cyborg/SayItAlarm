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
fun PanelColumnStandardPreview() {
    PanelStandard(
        panelItems = listOf(
            {
                PanelRowStandard(
                    valueLabel = stringResource(id = R.string.timeout),
                    value = stringResource(id = R.string.minute_short, 180),
                    afterContent = { IconButtonEdit {} },
                )
            },
            {
                PanelRowStandard(
                    valueLabel = stringResource(id = R.string.snooze),
                    value = stringResource(id = R.string.minute_short, 15),
                    afterContent = { IconButtonEdit {} },
                )
            },
            {
                PanelRowStandard(
                    valueLabel = stringResource(id = R.string.theme),
                    value = "Light",
                    afterContent = { IconButtonEdit {} },
                )
            },
        ),
    )
}
