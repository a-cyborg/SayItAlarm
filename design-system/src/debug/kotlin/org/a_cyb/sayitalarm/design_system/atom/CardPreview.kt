/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CardStandardPreview() {
    CardStandard {
        TextDisplayStandardLarge(text = "CardStandard")
    }
}

@Preview
@Composable
fun CardStandardCenteredPreview() {
    CardStandardCentered {
        TextDisplayStandardLarge(text = "CardStandardCentered")
    }
}
