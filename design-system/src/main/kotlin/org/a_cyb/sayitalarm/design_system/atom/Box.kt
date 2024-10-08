/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import org.a_cyb.sayitalarm.design_system.molecule.animateCircleBorder
import org.a_cyb.sayitalarm.design_system.token.Brush
import org.a_cyb.sayitalarm.design_system.token.Sizing

@Composable
fun BoxVerticalFading(content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalFading,
                    blendMode = BlendMode.DstIn,
                )
            },
    ) {
        content()
    }
}

@Composable
fun BoxAnimatedCircleBorder(
    brush: androidx.compose.ui.graphics.Brush,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(Sizing.CircleButton.Large)
            .animateCircleBorder(brush),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
