/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.atom.SpacerLarge
import org.a_cyb.sayitalarm.design_system.atom.TextBodyStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextBodySubtleMedium
import org.a_cyb.sayitalarm.design_system.atom.TextBodyWarningLarge
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayAttentionSmall
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayDangerSmall
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.design_system.atom.TextDisplaySubtleSmall
import org.a_cyb.sayitalarm.design_system.atom.TextDisplayWarningSmall
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleStandardMedium
import org.a_cyb.sayitalarm.design_system.atom.TextTitleSubtleLarge
import org.a_cyb.sayitalarm.design_system.atom.TextTitleWarningMedium
import org.a_cyb.sayitalarm.design_system.token.Spacing

@Composable
fun TextBoxTitleAndInfo(title: String, info: String = "") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = Spacing.m),
    ) {
        TextTitleStandardLarge(text = title)
        SpacerLarge()
        TextBodyStandardLarge(text = info)
    }
}

@Composable
fun TextBoxInfo(text: String) {
    Box(Modifier.padding(Spacing.m)) {
        TextTitleStandardMedium(text = text)
    }
}

@Composable
fun TextBoxWarningTitle(text: String) {
    Box(modifier = Modifier.padding(Spacing.m)) {
        TextTitleWarningMedium(text = text)
    }
}

@Composable
fun TextBoxWarningBody(text: String) {
    Box(modifier = Modifier.padding(Spacing.m)) {
        TextBodyWarningLarge(text = text)
    }
}

@Composable
fun TextBoxStandardBody(text: String) {
    Box(Modifier.padding(Spacing.m)) {
        TextTitleStandardMedium(text = text)
    }
}

@Composable
fun TextBoxSayItScript(text: String) {
    Box(
        modifier = Modifier.padding(Spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        TextTitleStandardLarge(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
fun TextBoxSttResult(text: String) {
    Box(
        modifier = Modifier.padding(horizontal = Spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        TextTitleSubtleLarge(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
fun TextBoxStatusHeaderReady(count: String) {
    TextRowHeaderStatusBox(
        headerTitle = { TextDisplayStandardSmall(text = stringResource(id = R.string.ready)) },
        tailInfo = count,
    )
}

@Composable
fun TextBoxStatusHeaderListening(count: String) {
    TextRowHeaderStatusBox(
        headerTitle = { TextDisplaySubtleSmall(stringResource(id = R.string.listening)) },
        tailInfo = count,
    )
}

@Composable
fun TextBoxStatusHeaderSuccess(count: String) {
    TextRowHeaderStatusBox(
        headerTitle = { TextDisplayAttentionSmall(stringResource(id = R.string.success)) },
        tailInfo = count,
    )
}

@Composable
fun TextBoxStatusHeaderFailed(count: String) {
    TextRowHeaderStatusBox(
        headerTitle = { TextDisplayDangerSmall(text = stringResource(id = R.string.failed)) },
        tailInfo = count,
    )
}

@Composable
fun TextBoxStatusHeaderError() {
    TextRowHeaderStatusBox(
        headerTitle = { TextDisplayWarningSmall(text = stringResource(id = R.string.error)) },
        tailInfo = "",
    )
}

@Composable
private fun TextRowHeaderStatusBox(
    headerTitle: @Composable () -> Unit,
    tailInfo: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        headerTitle()
        Box(modifier = Modifier.align(Alignment.BottomEnd)) {
            TextBodySubtleMedium(text = tailInfo)
        }
    }
}
