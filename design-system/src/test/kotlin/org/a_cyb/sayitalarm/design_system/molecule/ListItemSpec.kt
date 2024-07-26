/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.a_cyb.sayitalarm.design_system.atom.IconButtonDelete
import org.a_cyb.sayitalarm.design_system.atom.IconButtonEdit
import org.a_cyb.sayitalarm.design_system.atom.SpacerMedium
import org.a_cyb.sayitalarm.design_system.atom.SwitchStandard
import org.a_cyb.sayitalarm.design_system.atom.TextHeadlineStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardMedium
import org.junit.Test

class ListItemSpec : RoborazziTest() {
    @Test
    fun `It renders ListItem`() {
        subjectUnderTest.setContent {
            ListItemStandard(
                content = {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        TextHeadlineStandardLarge(text = "11:33 PM")
                        TextTitleStandardMedium(text = "Wake Up, every weekday")
                        SpacerMedium()
                    }
                },
                afterContent = { SwitchStandard(checked = true) {} }
            )
        }
    }

    @Test
    fun `It renders AlarmListItem with before and after contents`() {
        subjectUnderTest.setContent {
            ListItemStandard(
                beforeContent = { IconButtonDelete {} },
                content = {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        TextHeadlineStandardLarge(text = "11:33 PM")
                        TextTitleStandardMedium(text = "Wake Up, every weekday")
                        SpacerMedium()
                    }
                },
                afterContent = { IconButtonEdit {} }
            )
        }
    }
}
