/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.token.Sizing

@Composable
fun PanelRowStandard(
    valueLabel: String,
    value: String = "",
    afterContent: @Composable () -> Unit = { SpacerMedium() },
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = Sizing.PanelRow.MinHeight),
    ) {
        TextBodyStandardLarge(text = valueLabel)
        Spacer(Modifier.weight(1f))
        TextBodySubtleMedium(text = value)
        afterContent()
    }
}
