/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import org.a_cyb.sayitalarm.design_system.atom.TextDisplaySubtleSmall
import org.a_cyb.sayitalarm.design_system.atom.TextHeadlineStandardSmall
import org.a_cyb.sayitalarm.design_system.token.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSmall(
    title: String,
    firstIcon: @Composable () -> Unit,
    secondIcon: @Composable () -> Unit = {},
    thirdIcon: @Composable () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { TextHeadlineStandardSmall(text = title) },
        navigationIcon = { firstIcon() },
        actions = {
            secondIcon()
            thirdIcon()
        },
        windowInsets = WindowInsets.statusBars,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.surface.standard,
            navigationIconContentColor = Color.text.accent,
            actionIconContentColor = Color.text.accent,
            scrolledContainerColor = Color.surface.standard,
            titleContentColor = Color.surface.standard,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMedium(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    MediumTopAppBar(
        title = { TextDisplaySubtleSmall(text = title) },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.surface.standard,
            navigationIconContentColor = Color.text.accent,
            actionIconContentColor = Color.text.accent,
            scrolledContainerColor = Color.surface.standard,
            titleContentColor = Color.surface.standard,
        ),
    )
}
