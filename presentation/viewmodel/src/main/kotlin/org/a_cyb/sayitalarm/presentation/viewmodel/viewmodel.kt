/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel as AndroidViewModel
import androidx.lifecycle.viewModelScope

abstract class ViewModel : AndroidViewModel() {
    val scope: CoroutineScope = viewModelScope
}