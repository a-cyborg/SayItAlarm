/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.molecule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.a_cyb.sayitalarm.R
import org.a_cyb.sayitalarm.atom.BoxVerticalFading
import org.a_cyb.sayitalarm.atom.SpacerMedium
import org.a_cyb.sayitalarm.token.Sizing

@Suppress("MagicNumber")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelPicker(
    values: List<T>,
    initIdx: Int = 0,
    itemRow: @Composable (T) -> Unit,
    onCancel: () -> Unit,
    onConfirm: (idx: Int) -> Unit,
) {
    val itemRowHeight = Sizing.WheelPicker.SmallRowHeight
    val visibleItemNum: Int = 5
    val pickerHeight = remember { itemRowHeight * visibleItemNum }
    val placeholderHeight = remember { itemRowHeight * (visibleItemNum / 2) }
    val pickerContentDescription = stringResource(id = R.string.component_wheel_picker)

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .semantics { contentDescription = pickerContentDescription },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BoxVerticalFading {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pickerHeight),
                state = lazyListState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
                contentPadding = PaddingValues(all = 0.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // This item serves as a placeholder for the first item to be vertically centered.
                item { Spacer(modifier = Modifier.height(placeholderHeight)) }

                items(values) {
                    Row(
                        modifier = Modifier.height(itemRowHeight),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        itemRow(it)
                    }
                }

                item { Spacer(modifier = Modifier.height(placeholderHeight)) }
            }
        }
        SpacerMedium()

        fun getSelectedItemIdx(): Int = when (lazyListState.firstVisibleItemIndex) {
            0 -> if (lazyListState.firstVisibleItemScrollOffset == 0) 0 else 1
            else -> lazyListState.firstVisibleItemIndex + 1
        }

        ActionRowCancelAndConfirm(
            onCancel = onCancel,
            onConfirm = { onConfirm(getSelectedItemIdx()) },
        )

        val density = LocalDensity.current.density
        LaunchedEffect(Unit) {
            val rowHeightPx by lazy { itemRowHeight.value.toPx(density) }
            // Note: This implementation is optimized for 5 visible items.
            // Later to support a different number of items,
            // adjust the idx, scrollOffset calculation logic accordingly.
            // The scrollOffset should be a multiple of rowHeightPx,
            // based on the number of visible items above.
            val idx = if (initIdx == 0) initIdx else initIdx - 1
            val scrollOffset = if (initIdx == 1) rowHeightPx.toInt() else 0

            lazyListState.scrollToItem(idx, scrollOffset)
        }
    }
}

private fun Float.toPx(density: Float): Float = this * density
