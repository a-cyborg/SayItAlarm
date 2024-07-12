/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.implementations.permission_manager

interface PermissionManagerContract {
    interface PermissionCheckerContract {
        fun getMissingEssentialPermission(): List<String>
        fun getMissingPermission(): List<String>
    }
}
