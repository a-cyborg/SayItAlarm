/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.design_system.atom.SpacerLarge
import org.a_cyb.sayitalarm.design_system.atom.TextBodyStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardMedium
import org.a_cyb.sayitalarm.design_system.atom.TextTitleWarningMedium
import org.a_cyb.sayitalarm.design_system.token.Spacing

@Composable
fun TextRowTitleAndInfo(title: String, info: String = "") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = Spacing.m)
    ) {
        TextTitleStandardLarge(text = title)
        SpacerLarge()
        TextBodyStandardLarge(text = info)
    }
}

@Composable
fun TextRowInfo(text: String) {
    Box(Modifier.padding(Spacing.m)) {
        TextTitleStandardMedium(text = text)
    }
}

@Composable
fun TextRowWarning(text: String) {
    Box(modifier = Modifier.padding(Spacing.m)) {
        TextTitleWarningMedium(text = text)
    }
}
