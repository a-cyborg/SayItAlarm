/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemStandard

@Preview
@Composable
fun PanelColumnStandardPreview() {
    PanelStandard(
        {
            PanelItemStandard(
                valueLabel = stringResource(id = R.string.timeout),
                value = "180 min",
                afterContent = { IconButtonEdit {} },
            )
        },
        {
            PanelItemStandard(
                valueLabel = stringResource(id = R.string.snooze),
                value = "15 min",
                afterContent = { IconButtonEdit {} },
            )
        },
        {
            PanelItemStandard(
                valueLabel = stringResource(id = R.string.theme),
                value = "Light",
                afterContent = { IconButtonEdit {} },
            )
        },
    )
}
