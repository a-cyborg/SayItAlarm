/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TextBoxTitleAndInfoPreview() {
    TextBoxTitleAndInfo(title = "Title", info = "Additional information.")
}

@Preview
@Composable
fun TextBoxInfoPreview() {
    TextBoxInfo(text = "TextBoxInfo")
}

@Preview
@Composable
fun TextBoxWarningTitlePreview() {
    TextBoxWarningTitle(text = "TextBoxWarningTitle")
}

@Preview
@Composable
fun TextBoxWarningBodyPreview() {
    TextBoxWarningBody(text = "TextBoxWarningBody")
}

@Preview
@Composable
fun TextBoxSayItScriptPreview() {
    TextBoxSayItScript(text = "TextBoxSayItScript")
}

@Preview
@Composable
fun TextBoxSttResultPreview() {
    TextBoxSttResult(text = "TextBoxSttResult")
}

@Preview
@Composable
fun TextBoxStatusHeaderReadyPreview() {
    TextBoxStatusHeaderReady("3/7")
}

@Preview
@Composable
fun TextBoxStatusHeaderListeningPreview() {
    TextBoxStatusHeaderListening("3/7")
}

@Preview
@Composable
fun TextBoxStatusHeaderSuccessPreview() {
    TextBoxStatusHeaderSuccess("3/7")
}

@Preview
@Composable
fun TextBoxStatusHeaderFailedPreview() {
    TextBoxStatusHeaderFailed("3/7")
}

@Preview
@Composable
fun TextBoxStatusHeaderErrorPreview() {
    TextBoxStatusHeaderError()
}
