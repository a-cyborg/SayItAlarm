/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.design_system.token.Spacing

@Composable
fun SpacerXSmall() {
    Spacer(modifier = Modifier.size(Spacing.xs))
}

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.size(Spacing.s))
}

@Composable
fun SpacerMedium() {
    Spacer(modifier = Modifier.size(Spacing.m))
}

@Composable
fun SpacerLarge() {
    Spacer(modifier = Modifier.size(Spacing.l))
}

@Composable
fun SpacerXLarge() {
    Spacer(modifier = Modifier.size(Spacing.xl))
}

@Composable
fun SpacerXxxxLarge() {
    Spacer(modifier = Modifier.size(Spacing.xxxxl))
}
