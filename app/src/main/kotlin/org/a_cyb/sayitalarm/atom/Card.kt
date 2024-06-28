/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.token.Color
import org.a_cyb.sayitalarm.token.Elevation
import org.a_cyb.sayitalarm.token.Shape
import org.a_cyb.sayitalarm.token.Spacing

@Composable
fun CardStandard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.m, vertical = Spacing.l),
        shape = Shape.Card.primary,
        colors = CardDefaults.cardColors(containerColor = Color.surface.standard),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.secondLevel),
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun CardStandardCentered(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.l),
        shape = Shape.Card.primary,
        colors = CardDefaults.cardColors(containerColor = Color.surface.standard),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.secondLevel),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
}

@Composable
fun CardStandardCenteredScrollable(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.l)
            .verticalScroll(rememberScrollState()),
        shape = Shape.Card.primary,
        colors = CardDefaults.cardColors(containerColor = Color.surface.standard),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.secondLevel),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
}
