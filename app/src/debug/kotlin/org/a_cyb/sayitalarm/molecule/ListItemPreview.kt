/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.atom.SwitchStandard
import org.a_cyb.sayitalarm.atom.IconButtonDelete
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.TextHeadlineStandardLarge
import org.a_cyb.sayitalarm.atom.TextTitleStandardMedium
import org.a_cyb.sayitalarm.token.Color

private val alarmContent: @Composable RowScope.() -> Unit = {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.Center,
    ) {
        TextHeadlineStandardLarge(text = "11:33 PM")
        TextTitleStandardMedium(text = "Wake Up, every weekday")
        SpacerMedium()
    }
}

@Preview
@Composable
fun ListItemStandardPreview() {
    ListItemStandard(
        content = alarmContent,
        afterContent = { SwitchStandard(checked = true) {} }
    )
}

@Preview
@Composable
fun ListItemStandardWithBeforeAndAfterContentPreview() {
    ListItemStandard(
        beforeContent = { IconButtonDelete {} },
        content = alarmContent,
        afterContent = { IconButtonEdit {} },
    )
}

@Preview
@Composable
fun ListItemStandardDarkThemePreview() {
    Color.useDarkTheme()
    ListItemStandard(content = alarmContent)
}

@Preview
@Composable
fun ListItemStandardWithBeforeAndAfterContentDarkThemePreview() {
    ListItemStandard(
        beforeContent = { IconButtonDelete {} },
        content = alarmContent,
        afterContent = { IconButtonEdit {} },
    )
}
