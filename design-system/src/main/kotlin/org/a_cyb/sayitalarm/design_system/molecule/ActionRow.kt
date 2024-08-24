/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.a_cyb.sayitalarm.design_system.atom.TextButtonCancel
import org.a_cyb.sayitalarm.design_system.atom.IconButtonCollapse
import org.a_cyb.sayitalarm.design_system.atom.TextButtonConfirm

@Composable
fun ActionRowCancelAndConfirm(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButtonCancel { onCancel() }
        TextButtonConfirm { onConfirm() }
    }
}

@Composable
fun ActionRowCollapse(
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButtonCollapse { onClick() }
    }
}
