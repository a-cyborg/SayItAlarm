/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.screen

import androidx.compose.runtime.Composable
import org.a_cyb.sayitalarm.design_system.atom.ColumnScreenStandard
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardLarge

@Composable
fun SayItScreen() {
    ColumnScreenStandard {
        // TextDisplayStandardLarge(text = "Welcome to the SayIt screen.🔘.🔘")
        TextDisplayStandardLarge(text = "SayItScreen")
    }
}