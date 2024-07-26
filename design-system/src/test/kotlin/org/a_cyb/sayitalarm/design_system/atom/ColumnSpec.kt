/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.ui.test.onNodeWithText
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.RoborazziTest
import org.junit.Test

class ColumnSpec : RoborazziTest() {

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `It renders ColumnScreenStandard`() {
        subjectUnderTest.setContent {
            ColumnScreenStandardScrollable {
                TextTitleStandardLarge(text = "Column")
                TextTitleStandardLarge(text = "Screen")
                TextTitleStandardLarge(text = "Standard")
            }
        }

        with(subjectUnderTest) {
            onNodeWithText("Column").assertExists()
            onNodeWithText(("Screen")).assertExists()
            onNodeWithText("Standard").assertExists()
            onNodeWithText(getString(R.string.say_it)).assertExists()
        }
    }
}
