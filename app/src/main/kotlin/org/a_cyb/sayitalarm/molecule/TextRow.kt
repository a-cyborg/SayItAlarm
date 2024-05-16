/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.SpacerLarge
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.atom.TextBodyStandardLarge
import org.a_cyb.sayitalarm.atom.TextDisplayStandardSmall
import org.a_cyb.sayitalarm.atom.TextTitleStandardLarge
import org.a_cyb.sayitalarm.atom.TextTitleWarningMedium
import org.a_cyb.sayitalarm.token.Spacing

// @Suppress("MagicNumber")
// @Composable
// fun TextRowTimeFormattedStandard(combinedMinutes: CombinedMinutes) {
//     val formatted =
//         if (DateFormat.is24HourFormat(LocalContext.current)) {
//             "${combinedMinutes.hour} : ${combinedMinutes.minute}"
//         }
//         else {
//             val isMorning = combinedMinutes.hour > 12
//         }
//
// }
@Suppress("MagicNumber")
@Composable
fun TextRowTimeDuration(minutes: Int) {
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (minutes / 60 > 0) {
            TextDisplayStandardSmall(text = (minutes / 60).toString())
            TextTitleStandardLarge(text = stringResource(id = R.string.hour_abbr))
            SpacerMedium()
        }
        TextDisplayStandardSmall(text = "%02d".format(minutes % 60))
        TextTitleStandardLarge(text = stringResource(id = R.string.minute_abbr))
    }
}

@Composable
fun TextRowTitleAndInfo(title: String, info: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextTitleStandardLarge(text = title)
        SpacerLarge()
        TextBodyStandardLarge(text = info)
    }
}

@Composable
fun TextRowInfo(text: String) {
    Box(Modifier.padding(Spacing.m)) {
        TextTitleStandardLarge(text = text)
    }
}

@Composable
fun TextRowWarning(text: String) {
    Box(modifier = Modifier.padding(Spacing.m)) {
        TextTitleWarningMedium(text = text)
    }
}
