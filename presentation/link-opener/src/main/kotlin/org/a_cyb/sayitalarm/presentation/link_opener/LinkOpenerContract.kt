/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.link_opener

interface LinkOpenerContract {
    fun openBrowserLink(url: String)
    fun openEmail(address: String, subject: String?)
    fun openGooglePlay()
}