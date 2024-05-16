/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.atom.IconButtonDelete
import org.a_cyb.sayitalarm.atom.IconButtonEdit
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class AlarmListItemSpec : RoborazziTest() {
    @Test
    fun `It renders AlarmListItem`() {
        subjectUnderTest.setContent {
            AlarmListItem(
                time = "8:30",
                weeklyRepeat = "Wakeup Call, Everyday",
            )
        }
    }

    @Test
    fun `It renders AlarmListItem with before and after contents`() {
        subjectUnderTest.setContent {
            AlarmListItem(
                time = "8:30",
                weeklyRepeat = "Wakeup Call, Everyday",
                beforeContent = { IconButtonDelete {} },
                afterContent = { IconButtonEdit {} },
            )
        }
    }
}
