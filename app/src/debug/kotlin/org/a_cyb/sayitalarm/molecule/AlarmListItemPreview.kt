/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.atom.IconButtonDelete
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.token.Color

@Preview
@Composable
fun AlarmListItemPreview() {
    AlarmListItem(
        time = "8:30",
        weeklyRepeat = "Wakeup Call, Everyday",
    )
}

@Preview
@Composable
fun AlarmListItemEditModePreview() {
    AlarmListItem(
        time = "8:30",
        weeklyRepeat = "Wakeup Call, Everyday",
        beforeContent = { IconButtonDelete {} },
        afterContent = { IconButtonEdit {} },
    )
}

@Preview
@Composable
fun AlarmListItemDarkThemePreview() {
    Color.useDarkTheme()
    AlarmListItem(
        time = "8:30",
        weeklyRepeat = "Wakeup Call, Everyday",
    )
}

@Preview
@Composable
fun AlarmListItemEditModeDarkThemePreview() {
    AlarmListItem(
        time = "8:30",
        weeklyRepeat = "Wakeup Call, Everyday",
        beforeContent = { IconButtonDelete {} },
        afterContent = { IconButtonEdit {} },
    )
}
