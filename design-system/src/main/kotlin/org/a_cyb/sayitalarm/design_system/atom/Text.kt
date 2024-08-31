/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.design_system.token.Font

@Composable
fun TextDisplayStandardLarge(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.display.l,
    )
}

@Composable
fun TextDisplayStandardSmall(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.display.s,
    )
}

@Composable
fun TextDisplaySubtleSmall(text: String) {
    Text(
        text = text,
        color = Color.text.subtle,
        style = Font.display.s,
    )
}

@Composable
fun TextDisplayAttentionSmall(text: String) {
    Text(
        text = text,
        color = Color.text.attention,
        style = Font.display.s,
    )
}

@Composable
fun TextDisplayDangerSmall(text: String) {
    Text(
        text = text,
        color = Color.text.danger,
        style = Font.display.s,
    )
}

@Composable
fun TextDisplayWarningSmall(text: String) {
    Text(
        text = text,
        color = Color.text.warning,
        style = Font.display.s,
    )
}

@Composable
fun TextHeadlineStandardLarge(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.headline.l,
    )
}

@Composable
fun TextHeadlineStandardSmall(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.headline.s,
    )
}

@Composable
fun TextTitleStandardLarge(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.title.l,
    )
}

@Composable
fun TextTitleAttentionLarge(text: String) {
    Text(
        text = text,
        color = Color.text.attention,
        style = Font.title.l,
    )
}

@Composable
fun TextTitleSubtleLarge(text: String) {
    Text(
        text = text,
        color = Color.text.subtle,
        style = Font.title.l,
    )
}

@Composable
fun TextTitleWarningLarge(text: String) {
    Text(
        text = text,
        color = Color.text.warning,
        style = Font.title.l,
    )
}


@Composable
fun TextTitleStandardMedium(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.title.m,
    )
}

@Composable
fun TextTitleAttentionMedium(text: String) {
    Text(
        text = text,
        color = Color.text.attention,
        style = Font.title.m,
    )
}


@Composable
fun TextTitleWarningMedium(text: String) {
    Text(
        text = text,
        color = Color.text.warning,
        style = Font.title.m,
    )
}

@Composable
fun TextTitleDangerMedium(text: String) {
    Text(
        text = text,
        color = Color.text.danger,
        style = Font.title.m,
    )
}

@Composable
fun TextLabelAttentionLarge(text: String) {
    Text(
        text = text,
        color = Color.text.attention,
        style = Font.label.l,
    )
}

@Composable
fun TextBodyStandardLarge(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.body.l,
    )
}

@Composable
fun TextBodyWarningLarge(text: String) {
    Text(
        text = text,
        color = Color.text.warning,
        style = Font.body.l,
    )
}

@Composable
fun TextBodySubtleMedium(text: String) {
    Text(
        text = text,
        color = Color.text.subtle,
        style = Font.body.m,
    )
}

@Composable
fun TextBodySubtleMediumUnderline(text: String) {
    Text(
        text = text,
        color = Color.text.subtle,
        style = Font.body.m.copy(textDecoration = TextDecoration.Underline)
    )
}

@Composable
fun TextBodyStandardSmall(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.body.s,
    )
}

@Composable
fun TextBodyStandardSmallUnderline(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.body.s.copy(textDecoration = TextDecoration.Underline),
    )
}
