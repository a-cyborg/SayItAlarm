/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.design_system.token.Color

// https://medium.com/@mohammadbahadori99/creating-an-animated-border-composable-in-jetpack-compose-8604e4c7c66e
@Composable
fun Modifier.animateCircleBorder(brush: Brush): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "CircleButtonBorder")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing)
        ),
        label = "CircleAnimation"
    )

    return this
        .clip(CircleShape)
        .padding(1.dp)
        .drawWithContent {
            rotate(angle) {
                drawCircle(
                    brush = brush,
                    radius = size.width,
                    blendMode = BlendMode.SrcIn,
                )
            }
            drawContent()
        }
        .background(color = Color.surface.standard, shape = CircleShape)
}

@Composable
fun Modifier.animateSlowFlickering(): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "Flickering")
    val flickeringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.33f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "FlickeringAnimateFloat"
    )

    return this.alpha(flickeringAlpha)
}