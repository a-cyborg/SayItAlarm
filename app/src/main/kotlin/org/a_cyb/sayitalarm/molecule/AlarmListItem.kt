/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.TextBodyStandardLarge
import org.a_cyb.sayitalarm.atom.TextHeadlineStandardLarge
import org.a_cyb.sayitalarm.token.Color

@Composable
fun AlarmListItem(
    time: String,
    weeklyRepeat: String,
    beforeContent: @Composable () -> Unit = {},
    afterContent: @Composable () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Color.surface.standard),
    ) {
        beforeContent()
        SpacerMedium()

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            TextHeadlineStandardLarge(text = time)
            TextBodyStandardLarge(text = weeklyRepeat)
            SpacerMedium()
        }
        afterContent()
        SpacerMedium()
    }
}
