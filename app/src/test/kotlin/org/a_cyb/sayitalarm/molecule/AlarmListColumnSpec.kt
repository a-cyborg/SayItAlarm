/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.app.atom.SwitchStandard
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class AlarmListColumnSpec : RoborazziTest() {

    private val alarmListItems: List<@Composable () -> Unit> = (0..6).map {
        {
            AlarmListItem(
                time = "${it * 3 / 12}:3$it",
                weeklyRepeat = "Wakeup Call[$it], Everyday ",
                afterContent = { SwitchStandard(checked = it % 2 == 0) {} },
            )
        }
    }

    @Test
    fun `It renders AlarmListColumn`() {
        subjectUnderTest.setContent {
            AlarmListColumn(alarmListItems = alarmListItems)
        }
    }
}
