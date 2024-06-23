/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.atom.SpacerLarge
import org.a_cyb.sayitalarm.atom.TextBodyStandardLarge
import org.a_cyb.sayitalarm.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.atom.TextTitleStandardMedium
import org.a_cyb.sayitalarm.atom.TextTitleWarningMedium
import org.a_cyb.sayitalarm.token.Spacing

@Composable
fun TextRowTitleAndInfo(title: String, info: String = "") {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
