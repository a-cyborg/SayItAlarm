/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.database

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

internal object DatabaseFactory {
    fun getInstance(context: Context): SayItDB {
        val driver = AndroidSqliteDriver(
            schema = SayItDB.Schema,
            context = context,
            name = "SayItDB",
        )

        return SayItDB.invoke(driver)
    }
}
