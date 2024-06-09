/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.token.Spacing

@Preview
@Composable
fun CardStandardPreview() {
    CardStandard {
        TextDisplayStandardLarge(text = "CardStandard")
    }
}

@Preview
@Composable
fun CardStandardCenteredPreview() {
    CardStandardCentered {
        TextDisplayStandardLarge(text = "CardStandardCentered")
    }
}

@Preview
@Composable
fun test() {
    CardStandardCentered {
        Box(modifier = Modifier.padding(all = Spacing.m)) {
            TextTitleStandardMedium (text = stringResource(id = R.string.info_list_no_alarm))
        }
    }
}

