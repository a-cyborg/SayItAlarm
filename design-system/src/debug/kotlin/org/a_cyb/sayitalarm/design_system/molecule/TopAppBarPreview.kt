/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.design_system.atom.IconButtonAdd
import org.a_cyb.sayitalarm.design_system.atom.IconButtonSettings
import org.a_cyb.sayitalarm.design_system.atom.TextButtonEdit

@Preview
@Composable
fun TopAppBarSmallPreview() {
    TopAppBarSmall(
        title = "SayIt",
        firstIcon = { TextButtonEdit {} },
        secondIcon = { IconButtonAdd {} },
        thirdIcon = { IconButtonSettings {} },
    )
}

@Preview
@Composable
fun TopAppBarLargePreview() {
    TopAppBarLarge(
        title = "SayIt",
        actions = {
            TextButtonEdit {}
            IconButtonAdd {}
            IconButtonSettings {}
        }
    )
}
