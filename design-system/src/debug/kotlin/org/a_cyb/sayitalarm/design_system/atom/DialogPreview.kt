/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */


package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.design_system.molecule.TopAppBarSmall

@Preview
@Composable
fun DialogStandardFillMaxPreview() {
    DialogStandardFillMax(
        onDismiss = {},
        topAppBar = {
            TopAppBarSmall(
                title = "Dialog",
                firstIcon = { IconButtonClose {} },
                secondIcon = { },
            )
        },
    ) {}
}

@Preview
@Composable
fun DialogStandardFitContentPreview() {
    DialogStandardFitContent(onDismiss = { }) {
        TextTitleStandardLarge(text = "Dialog")
    }
}
