/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.acyb.sayitalarm.database.SettingsQueries
import org.acyb.sayitalarm.database.Get as SettingsDTO

class SettingsDataSource(
    private val settingsQueries: SettingsQueries
) : DataSourceContract.SettingsDataSource {

    override fun getSettings(dispatcher: CoroutineDispatcher): Flow<Result<SettingsDTO>> =
        settingsQueries.get()
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::mapToResult)
            .catch { emit(Result.failure(it)) }

    private fun mapToResult(settings: SettingsDTO?): Result<SettingsDTO> {
        return settings
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException())
    }

    override suspend fun insert(settings: SettingsDTO) {
        settingsQueries.insert(
            settings.timeOut,
            settings.snooze,
            settings.theme
        )
    }

    override suspend fun setTimeOut(timeOut: Long) {
        settingsQueries.updateTimeOut(timeOut)
    }

    override suspend fun setSnooze(snooze: Long) {
        settingsQueries.updateSnooze(snooze)
    }

    override suspend fun setTheme(theme: Long) {
        settingsQueries.updateTheme(theme)
    }
}
