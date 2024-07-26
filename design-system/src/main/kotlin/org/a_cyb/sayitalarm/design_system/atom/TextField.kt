/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.design_system.token.Font

@Composable
fun TextFieldLabel(
    value: String,
    hint: String = "",
    textAlign: TextAlign = TextAlign.Start,
    onDone: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var text by rememberSaveable(value) { mutableStateOf(value) }

    TextField(
        value = text,
        onValueChange = { text = it },
        textStyle = Font.body.l.copy(textAlign = textAlign),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.surface.subtle.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.surface.subtle,
            unfocusedContainerColor = Color.surface.standard,
            unfocusedIndicatorColor = Color.ColorPalette.transparent
        ),
        placeholder = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextBodySubtleMedium(text = hint)
            }
        },
        maxLines = 2,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone(text)
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TextFieldSayItScript(
    text: String,
    onValueChange: (String) -> Unit,
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val height = (screenHeightDp * 0.5).dp

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .focusRequester(focusRequester),
        textStyle = Font.body.l,
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.surface.subtle.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.surface.subtle,
            unfocusedContainerColor = Color.surface.standard,
            unfocusedIndicatorColor = Color.ColorPalette.transparent,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        placeholder = { TextBodySubtleMedium(text = stringResource(id = R.string.say_it)) }
    )
}
