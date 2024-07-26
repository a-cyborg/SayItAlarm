/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.token

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.robolectric.annotation.Config
import androidx.compose.ui.graphics.Color as ColorPalette

@Config(sdk = [33])
class ColorSpec {

    @TestFactory
    fun `Color is initially in LightMode`(): List<DynamicTest> {
        return listOf(
            ColorModeTestSpec("text.standard", ColorPalette(0xFF000000)) { Color.text.standard },
            ColorModeTestSpec("text.subtle", ColorPalette(0xFF7C7C7C)) { Color.text.subtle },
            ColorModeTestSpec("text.inverse", ColorPalette(0xFFFFFFFF)) { Color.text.inverse },
            ColorModeTestSpec("text.success", ColorPalette(0xFF79740E)) { Color.text.success },
            ColorModeTestSpec("text.info", ColorPalette(0xFF076678)) { Color.text.info },
            ColorModeTestSpec("text.warning", ColorPalette(0xFF8F3F71)) { Color.text.warning },
            ColorModeTestSpec("text.danger", ColorPalette(0xFF9D0006)) { Color.text.danger },
            ColorModeTestSpec("text.accent", ColorPalette(0xFFB57614)) { Color.text.accent },
            ColorModeTestSpec("text.attention", ColorPalette(0xFF427658)) { Color.text.attention },
            ColorModeTestSpec("surface.standard", ColorPalette(0xFFFFFFFF)) { Color.surface.standard },
            ColorModeTestSpec("surface.subtle", ColorPalette(0xFFADADAD)) { Color.surface.subtle },
            ColorModeTestSpec("surface.strong", ColorPalette(0xFF999999)) { Color.surface.strong },
            ColorModeTestSpec("surface.success", ColorPalette(0xFF79740E)) { Color.surface.success },
            ColorModeTestSpec("surface.accent", ColorPalette(0xFFB57614)) { Color.surface.accent },
            ColorModeTestSpec("surface.attention", ColorPalette(0xFF427658)) { Color.surface.attention },
        ).map { (descriptor, expected, actual) ->
            dynamicTest("Color.$descriptor is initially in LightMode") {
                assertEquals(actual(), expected)
            }
        }
    }

    @TestFactory
    fun `When useDarkMode is called it switches in DarkMode`(): List<DynamicTest> {
        Color.useLightTheme()
        Color.useDarkTheme()

        return listOf(
            ColorModeTestSpec("text.standard", ColorPalette(0xFFFFFFFF)) { Color.text.standard },
            ColorModeTestSpec("text.subtle", ColorPalette(0xFF999999)) { Color.text.subtle },
            ColorModeTestSpec("text.inverse", ColorPalette(0xFF000000)) { Color.text.inverse },
            ColorModeTestSpec("text.success", ColorPalette(0xFF98971A)) { Color.text.success },
            ColorModeTestSpec("text.info", ColorPalette(0xFF458588)) { Color.text.info },
            ColorModeTestSpec("text.warning", ColorPalette(0xFFB16286)) { Color.text.warning },
            ColorModeTestSpec("text.danger", ColorPalette(0xFFCC241D)) { Color.text.danger },
            ColorModeTestSpec("text.accent", ColorPalette(0xFFD79921)) { Color.text.accent },
            ColorModeTestSpec("text.attention", ColorPalette(0xFF689D6A)) { Color.text.attention },
            ColorModeTestSpec("surface.standard", ColorPalette(0xFF282828)) { Color.surface.standard },
            ColorModeTestSpec("surface.subtle", ColorPalette(0xFF7C7C7C)) { Color.surface.subtle },
            ColorModeTestSpec("surface.strong", ColorPalette(0xFF000000)) { Color.surface.strong },
            ColorModeTestSpec("surface.success", ColorPalette(0xFF98971A)) { Color.surface.success },
            ColorModeTestSpec("surface.accent", ColorPalette(0xFFD79921)) { Color.surface.accent },
            ColorModeTestSpec("surface.attention", ColorPalette(0xFF689D6A)) { Color.surface.attention },
        ).map { (descriptor, expected, actual) ->
            dynamicTest("When useDarkMode is called Color.$descriptor is in DarkMode") {
                assertEquals(actual(), expected)
            }
        }
    }

    @TestFactory
    fun `When useLightMode is called it switches in LightMode`(): List<DynamicTest> {
        Color.useDarkTheme()
        Color.useLightTheme()

        return listOf(
            ColorModeTestSpec("text.standard", ColorPalette(0xFF000000)) { Color.text.standard },
            ColorModeTestSpec("text.subtle", ColorPalette(0xFF7C7C7C)) { Color.text.subtle },
            ColorModeTestSpec("text.inverse", ColorPalette(0xFFFFFFFF)) { Color.text.inverse },
            ColorModeTestSpec("text.success", ColorPalette(0xFF79740E)) { Color.text.success },
            ColorModeTestSpec("text.info", ColorPalette(0xFF076678)) { Color.text.info },
            ColorModeTestSpec("text.warning", ColorPalette(0xFF8F3F71)) { Color.text.warning },
            ColorModeTestSpec("text.danger", ColorPalette(0xFF9D0006)) { Color.text.danger },
            ColorModeTestSpec("text.accent", ColorPalette(0xFFB57614)) { Color.text.accent },
            ColorModeTestSpec("text.attention", ColorPalette(0xFF427658)) { Color.text.attention },
            ColorModeTestSpec("surface.standard", ColorPalette(0xFFFFFFFF)) { Color.surface.standard },
            ColorModeTestSpec("surface.subtle", ColorPalette(0xFFADADAD)) { Color.surface.subtle },
            ColorModeTestSpec("surface.strong", ColorPalette(0xFF999999)) { Color.surface.strong },
            ColorModeTestSpec("surface.success", ColorPalette(0xFF79740E)) { Color.surface.success },
            ColorModeTestSpec("surface.accent", ColorPalette(0xFFB57614)) { Color.surface.accent },
            ColorModeTestSpec("surface.attention", ColorPalette(0xFF427658)) { Color.surface.attention },
        ).map { (descriptor, expected, actual) ->
            dynamicTest("When useLightMode is called Color.$descriptor is in LightMode") {
                assertEquals(actual(), expected)
            }
        }
    }
}

data class ColorModeTestSpec(
    val descriptor: String,
    val expectedColor: ColorPalette,
    val actualColor: () -> ColorPalette,
)
