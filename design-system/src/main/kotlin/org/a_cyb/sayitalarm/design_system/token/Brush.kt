/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.token

import androidx.compose.ui.graphics.Brush

@Suppress("MagicNumber")
object Brush {
    val verticalFading = Brush.verticalGradient(
        0f to Color.ColorPalette.transparent,
        0.3f to Color.ColorPalette.transparent.copy(alpha = 0.3f),
        0.5f to Color.surface.standard,
        0.7f to Color.ColorPalette.transparent.copy(alpha = 0.3f),
        1f to Color.ColorPalette.transparent,
    )

    val sweepGradientRainbow = Brush.sweepGradient(
        listOf(
            Color.text.danger,
            Color.text.warning,
            Color.text.accent,
            Color.text.success,
            Color.text.warning,
            Color.text.info,
            Color.text.attention,
            Color.text.inverse,
            Color.text.subtle,
        )
    )

    val sweepGradientGray = Brush.sweepGradient(
        listOf(
            Color.surface.standard,
            Color.surface.subtle,
            Color.surface.strong,
        )
    )

    val sweepGradientAttention = Brush.sweepGradient(
        listOf(
            Color.surface.standard,
            Color.surface.success,
            Color.surface.attention,
        )
    )

    val sweepGradientDanger = Brush.sweepGradient(
        listOf(
            Color.surface.standard,
            Color.text.warning,
            Color.text.danger,
        )
    )
}