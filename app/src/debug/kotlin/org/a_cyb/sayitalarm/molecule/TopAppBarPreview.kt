/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.atom.IconButtonAdd
import org.a_cyb.sayitalarm.atom.IconButtonEditText
import org.a_cyb.sayitalarm.atom.IconButtonSettings

@Preview
@Composable
fun TopAppBarSmallPreview() {
    TopAppBarSmall(
        title = "SayIt",
        firstIcon = { IconButtonEditText {} },
        secondIcon = { IconButtonAdd {} },
        thirdIcon = { IconButtonSettings {} },
    )
}

@Preview
@Composable
fun TopAppBarMediumPreview() {
    TopAppBarMedium(
        title = "SayIt",
        actions = {
            IconButtonEditText {}
            IconButtonAdd {}
            IconButtonSettings {}
        }
    )
}
