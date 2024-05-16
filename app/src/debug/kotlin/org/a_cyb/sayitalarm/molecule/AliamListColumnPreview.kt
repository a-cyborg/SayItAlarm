/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.app.atom.SwitchStandard

@Preview
@Composable
fun AlarmListColumnPreview() {
    val alarmListItems: List<@Composable () -> Unit> = (0..6).map {
        {
            AlarmListItem(
                time = "${it * 3 / 12}:3$it",
                weeklyRepeat = "Wakeup Call[$it], Everyday ",
                afterContent = { SwitchStandard(checked = it % 2 == 0) {} },
            )
        }
    }

    AlarmListColumn(alarmListItems = alarmListItems)
}
