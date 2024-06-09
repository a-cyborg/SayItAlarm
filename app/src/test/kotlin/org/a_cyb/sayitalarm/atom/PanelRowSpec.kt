/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.ui.res.stringResource
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class PanelRowSpec : RoborazziTest() {

    @Test
    fun `It renders PanelRowStandard`() {
        subjectUnderTest.setContent {
            PanelRowStandard(
                valueLabel = stringResource(id = R.string.timeout),
                value = "180 min",
                afterContent = { IconButtonEdit {} },
            )
        }
    }

    @Test
    fun `Given PanelRowSpec displays without a value`() {
        subjectUnderTest.setContent {
            PanelRowStandard(
                valueLabel = stringResource(id = R.string.about),
                afterContent = { IconButtonEdit {} },
            )
        }
    }

    @Test
    fun `Given PanelRowSpec displays without an afterContent`() {
        subjectUnderTest.setContent {
            PanelRowStandard(
                valueLabel = stringResource(id = R.string.version),
                value = "1.0",
            )
        }
    }
}
