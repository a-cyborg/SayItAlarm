/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TextRowTitleAndInfoPreview() {
    TextRowTitleAndInfo(title = "Title", info = "Additional information.")
}

@Preview
@Composable
fun TextRowInfoPreview() {
    TextRowInfo(text = "TextRowInfo")
}

@Preview
@Composable
fun TextRowWarningPreview() {
    TextRowWarning(text = "TextRowInfo")
}
