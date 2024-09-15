/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.token

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class BrushSpec : RoborazziTest() {
    @Test
    fun `It renders sweepGradientRainbow`() {
        subjectUnderTest.setContent {
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Brush.sweepGradientRainbow)
                    .size(Sizing.CircleButton.Large),
            )
        }
    }

    @Test
    fun `It renders sweepGradientGray`() {
        subjectUnderTest.setContent {
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Brush.sweepGradientGray)
                    .size(Sizing.CircleButton.Large),
            )
        }
    }

    @Test
    fun `It renders sweepGradientAttention`() {
        subjectUnderTest.setContent {
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Brush.sweepGradientAttention)
                    .size(Sizing.CircleButton.Large),
            )
        }
    }

    @Test
    fun `It renders sweepGradientDanger`() {
        subjectUnderTest.setContent {
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Brush.sweepGradientDanger)
                    .size(Sizing.CircleButton.Large),
            )
        }
    }
}
