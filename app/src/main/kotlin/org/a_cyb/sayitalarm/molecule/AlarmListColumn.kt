/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.DividerStandard
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.SpacerSmall
import org.a_cyb.sayitalarm.atom.TextHeadlineStandardSmall
import org.a_cyb.sayitalarm.token.Color

@Composable
fun AlarmListColumn(
    alarmListItems: List<@Composable () -> Unit>,
) {
    LazyColumn(
        modifier = Modifier.background(Color.surface.standard),
    ) {
        item {
            Row {
                SpacerMedium()
                TextHeadlineStandardSmall(text = stringResource(id = R.string.alarms))
            }
            SpacerSmall()
        }

        items(alarmListItems) {
            it()
            DividerStandard()
            SpacerSmall()
        }
    }
}
