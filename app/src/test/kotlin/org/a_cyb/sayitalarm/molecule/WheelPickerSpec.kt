/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.RoborazziTest
import org.a_cyb.sayitalarm.atom.TextHeadlineStandardLarge
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
    fun `When initIdx is not provided it displays a first value in the center`() {
        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
                onConfirm = { _ -> },
            )
        }

        with(subjectUnderTest) {
            onNodeWithText(colors[0]).assertExists()
            onNodeWithText(colors[1]).assertExists()
            onNodeWithText(colors[2]).assertExists()
            onNodeWithText(colors[3]).assertDoesNotExist()
        }
    }

    @Test
    fun `When initIdx is provided it displays a value at initIdx in the center`() {
        val initIdx = 6

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
            onNodeWithText(colors[initIdx - 2]).assertExists()
            onNodeWithText(colors[initIdx - 1]).assertExists()
            onNodeWithText(colors[initIdx]).assertExists()
            onNodeWithText(colors[initIdx + 1]).assertExists()
            onNodeWithText(colors[initIdx + 2]).assertExists()
        }
    }

    @Test
    fun `When 1 is provided for the initIdx it displays a second value in the center`() {
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
    fun `When last value is centered it displays only value before`() {
        val initIdx = colors.lastIndex

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
            onNodeWithText(colors[initIdx - 2]).assertExists()
            onNodeWithText(colors[initIdx - 1]).assertExists()
            onNodeWithText(colors[initIdx]).assertExists()
        }
    }

    @Test
    fun `When onConfirm called it propagates the given action with selected value`() {
        val selectedItemIdx = 3

        var hasBeenCalled = false
        var selectedItem = ""

        subjectUnderTest.setContent {
            WheelPicker(
                values = colors,
                initIdx = selectedItemIdx,
                itemRow = { TextHeadlineStandardLarge(text = it) },
                onCancel = {},
            ) {
                hasBeenCalled = true
                selectedItem = it
            }
        }

        subjectUnderTest
            .onNodeWithText(getString(R.string.confirm))
            .performClick()

        assertTrue(hasBeenCalled)
        assertEquals(selectedItem, colors[selectedItemIdx])
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
}
