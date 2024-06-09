/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import kotlin.test.assertTrue
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.atom.TextHeadlineStandardLarge
import org.a_cyb.sayitalarm.util.mustBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class WheelPickerSpec : RoborazziTest() {

    private val colors: List<String> = listOf(
        "Red", "Blue", "Green", "Yellow", "Purple",
        "Orange", "Pink", "Brown", "Black", "White",
        "Gray", "Turquoise", "Maroon",
    )

    private fun getString(id: Int) = subjectUnderTest.activity.getString(id)

    @Test
    fun `It renders a WheelPicker`() {
        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
                onConfirm = { _ -> },
            )
        }
    }

    @Test
    fun `Given initIdx is provided it displays item of index as selected value `() {
        val initIdx = 6
        var currentItemIdx: Int? = null

        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                initIdx = initIdx,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
                onConfirm = { currentItemIdx = it },
            )
        }

        with(subjectUnderTest) {
            onNodeWithText(colors[initIdx - 3]).assertDoesNotExist()
            onNodeWithText(colors[initIdx - 2]).assertExists()
            onNodeWithText(colors[initIdx - 1]).assertExists()
            onNodeWithText(colors[initIdx]).assertExists()
            onNodeWithText(colors[initIdx + 1]).assertExists()
            onNodeWithText(colors[initIdx + 2]).assertExists()
            onNodeWithText(colors[initIdx + 3]).assertDoesNotExist()
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        currentItemIdx mustBe initIdx
    }

    @Test
    fun `When initIdx is not provided it sets initIdx to 0`() {
        var currentItemIdx: Int? = null

        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
                onConfirm = { currentItemIdx = it },
            )
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        currentItemIdx mustBe 0
    }

    @Test
    fun `Given initIdx to 1 it displays a second value in the center`() {
        val initIdx = 1

        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                initIdx = initIdx,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
                onConfirm = { _ -> },
            )
        }

        with(subjectUnderTest) {
            onNodeWithText(colors[initIdx - 1]).assertExists()
            onNodeWithText(colors[initIdx]).assertExists()
            onNodeWithText(colors[initIdx + 1]).assertExists()
            onNodeWithText(colors[initIdx + 2]).assertExists()
        }
    }

    @Test
    fun `Given onConfirm is called it propagates the given action with index of selected item`() {
        var hasBeenCalled = false
        var selectedItemIdx: Int? = null

        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                initIdx = 3,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
            ) {
                hasBeenCalled = true
                selectedItemIdx = it
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        hasBeenCalled mustBe true
        selectedItemIdx mustBe 3
    }

    @Test
    fun `When onCancel called it propagates the onCancel action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {
                    hasBeenCalled = true
                },
                onConfirm = { _ -> },
            )
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.cancel))
            .performClick()

        assertTrue(hasBeenCalled)
    }

    @Test
    fun `Given a scroll to value at the center and confirm is clicked it propagates an action with the index corresponding to the value`() {
        var selectedItemIdx: Int? = null

        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                initIdx = 3,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
            ) {
                selectedItemIdx = it
            }
        }

        subjectUnderTest.onNodeWithText(colors[3])
            .onParent()
            .performScrollToIndex(6)

        subjectUnderTest
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        selectedItemIdx mustBe 7
    }
}
