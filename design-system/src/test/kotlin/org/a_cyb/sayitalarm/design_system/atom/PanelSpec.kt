/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.design_system.molecule.PanelItemStandard
import org.junit.Test

class PanelSpec : RoborazziTest() {

    @Test
    fun `It renders PanelStandard`() {
        subjectUnderTest.setContent {
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
    }
}
