/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.util

import java.util.Locale

/*
* TODO: This implementation is basic. Update with a more efficient and robust solution in the future.
* https://www.youtube.com/watch?v=XYi2-LPrwm4
* */
fun calculateEditDistance(source: String, target: String): Int {
    val s = source.normalizeString()
    val t = target.normalizeString()
    val cache = Array(s.length + 1) { IntArray(t.length + 1) { Int.MAX_VALUE } }

    (0..t.length).forEach { cache[s.length][it] = t.length - it }
    (0..s.length).forEach { cache[it][t.length] = s.length - it }

    for (rowPtr in s.lastIndex downTo 0) {
        for (colPtr in t.lastIndex downTo 0) {
            if (s[rowPtr] == t[colPtr]) {
                cache[rowPtr][colPtr] = cache[rowPtr + 1][colPtr + 1]
            } else {
                cache[rowPtr][colPtr] = 1 + minOf(
                    cache[rowPtr + 1][colPtr],
                    cache[rowPtr][colPtr + 1],
                    cache[rowPtr + 1][colPtr + 1],
                )
            }
        }
    }

    return cache[0][0]
}

private fun String.normalizeString(): String =
    this.replace(" ", "")
        .replace(".", "")
        .lowercase(Locale.getDefault())
