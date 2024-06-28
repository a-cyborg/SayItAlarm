/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.atom.SwitchStandard
import org.a_cyb.sayitalarm.atom.IconButtonDelete
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.TextHeadlineStandardLarge
import org.a_cyb.sayitalarm.atom.TextTitleStandardMedium
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
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
