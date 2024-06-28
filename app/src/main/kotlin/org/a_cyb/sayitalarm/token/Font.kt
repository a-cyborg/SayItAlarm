package org.a_cyb.sayitalarm.token

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.a_cyb.sayitalarm.R

object Font {
    private val aliceFontFamily = FontFamily(
        Font(R.font.alice_regular, FontWeight.Normal),
    )
    private val angkorFontFamily = FontFamily(
        Font(R.font.angkor_regular, FontWeight.Normal),
    )
    private val outfitFontFamily = FontFamily(
        Font(R.font.outfit_light, FontWeight.Light),
        Font(R.font.outfit_regular, FontWeight.Normal),
    )
    private val base = TextStyle(fontFamily = aliceFontFamily)

    object Display {
        val l = base.copy(
            fontWeight = FontWeight.Light,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp,
        )

        val m = base.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 43.sp,
            lineHeight = 50.sp,
            letterSpacing = 0.sp,
        )

        val s = base.copy(
            fontWeight = FontWeight.ExtraLight,
            fontSize = 33.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp,
        )
    }

    val display = Display

    object Headline {
        private val headlineBase = TextStyle(fontFamily = aliceFontFamily)

        val l = headlineBase.copy(
            fontWeight = FontWeight.Light,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp,
        )

        val m = headlineBase.copy(
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp,
        )

        val s = headlineBase.copy(
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        )
    }

    val headline = Headline

    object Title {
        val l = base.copy(
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        )

        val m = base.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
        )

        val s = base.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        )
    }

    val title = Title

    object Label {
        private val base = TextStyle(fontFamily = angkorFontFamily)

        val l = base.copy(
            fontSize = 14.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.0.sp,
        )

        val m = base.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )

        val s = base.copy(
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )
    }

    val label = Label

    object Body {
        private val base = TextStyle(fontFamily = outfitFontFamily)

        val l = base.copy(
            fontSize = 17.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.6.sp,
        )

        val m = base.copy(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
        )

        val s = base.copy(
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
        )
    }

    val body = Body
}
