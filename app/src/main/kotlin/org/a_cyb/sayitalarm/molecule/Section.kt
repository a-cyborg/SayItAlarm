/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.SectionRowClickable
import org.a_cyb.sayitalarm.atom.TextDisplayStandardLarge

@Composable
fun TimeSection(
    time: String,
    onClick: () -> Unit,
) {
    SectionRowClickable(
        contentDescription = stringResource(id = R.string.action_set_alarm_time),
        onClick = onClick,
    ) {
        TextDisplayStandardLarge(text = time)
    }
}
