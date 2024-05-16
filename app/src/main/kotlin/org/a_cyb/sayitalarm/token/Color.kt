package org.a_cyb.sayitalarm.token

import androidx.compose.ui.graphics.Color

object Color {

    private var _surface: SurfaceColorScheme = SurfaceLight
    private var _text: TextColorScheme = TextLight

    fun useDarkTheme() {
        _surface = SurfaceDark
        _text = TextDark
    }

    fun useLightTheme() {
        _surface = SurfaceLight
        _text = TextLight
    }

    object SurfaceLight : SurfaceColorScheme {
        override val standard = ColorPalette.white
        override val subtle = ColorPalette.Secondary.Gray.faded
        override val strong = ColorPalette.Secondary.Gray.light
        override val success = ColorPalette.Secondary.Green.light
        override val accent = ColorPalette.Primary.Yellow.light
        override val attention: Color = ColorPalette.Primary.Aqua.light
    }

    object SurfaceDark : SurfaceColorScheme {
        override val standard = ColorPalette.Secondary.Gray.dark
        override val subtle = ColorPalette.Secondary.Gray.medium
        override val strong = ColorPalette.black
        override val success = ColorPalette.Secondary.Green.dark
        override val accent = ColorPalette.Primary.Yellow.dark
        override val attention: Color = ColorPalette.Primary.Aqua.dark
    }

    val surface: SurfaceColorScheme
        get() = _surface

    object TextLight : TextColorScheme {
        override val standard = ColorPalette.black
        override val subtle = ColorPalette.Secondary.Gray.medium
        override val inverse = ColorPalette.white
        override val success = ColorPalette.Secondary.Green.light
        override val info = ColorPalette.Secondary.Blue.light
        override val warning = ColorPalette.Secondary.Pink.light
        override val danger = ColorPalette.Secondary.Red.light
        override val accent = ColorPalette.Primary.Yellow.light
        override val attention = ColorPalette.Primary.Aqua.light
    }

    object TextDark : TextColorScheme {
        override val standard = ColorPalette.white
        override val subtle = ColorPalette.Secondary.Gray.light
        override val inverse = ColorPalette.black
        override val success = ColorPalette.Secondary.Green.dark
        override val info = ColorPalette.Secondary.Blue.dark
        override val warning = ColorPalette.Secondary.Pink.dark
        override val danger = ColorPalette.Secondary.Red.dark
        override val accent = ColorPalette.Primary.Yellow.dark
        override val attention = ColorPalette.Primary.Aqua.dark
    }

    val text: TextColorScheme
        get() = _text

    object ColorPalette {
        val white = Color(0xFFFFFFFF)
        val black = Color(0xFF000000)
        val transparent = Color(0x00000000)

        object Primary {
            object Yellow {
                val light = Color(0xFFB57614)
                val dark = Color(0xFFD79921)
            }

            object Aqua {
                val light = Color(0xFF427658)
                val dark = Color(0xFF689D6A)
            }
        }

        object Secondary {
            object Red {
                val light = Color(0xFF9D0006)
                val dark = Color(0xFFCC241D)
            }

            object Green {
                val light = Color(0xFF79740E)
                val dark = Color(0xFF98971A)
            }

            object Blue {
                val light = Color(0xFF076678)
                val dark = Color(0xFF458588)
            }

            object Pink {
                val light = Color(0xFF8F3F71)
                val dark = Color(0xFFB16286)
            }

            object Gray {
                val faded = Color(0xFFADADAD)
                val light = Color(0xFF999999)
                val medium = Color(0xFF7C7C7C)
                val dark = Color(0xFF282828)
            }
        }
    }
}

interface SurfaceColorScheme {
    val standard: Color
    val subtle: Color
    val strong: Color
    val success: Color
    val accent: Color
    val attention: Color
}

interface TextColorScheme {
    val standard: Color
    val subtle: Color
    val inverse: Color
    val success: Color
    val info: Color
    val warning: Color
    val danger: Color
    val accent: Color
    val attention: Color
}
