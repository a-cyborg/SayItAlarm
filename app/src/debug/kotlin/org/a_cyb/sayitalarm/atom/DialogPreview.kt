/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */


package org.a_cyb.sayitalarm.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.molecule.TopAppBarGlobal

@Preview
@Composable
fun DialogStandardFillMaxPreview() {
    DialogStandardFillMax(
        onDismiss = {},
        topAppBar = {
            TopAppBarGlobal(
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
