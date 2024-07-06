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
import org.a_cyb.sayitalarm.data.model.SettingsEntity
import org.acyb.sayitalarm.database.SettingsQueries
import org.acyb.sayitalarm.database.Get as DTO  // This is an SQL(delight) generated Data Transfer Object for the get query.

class SettingsDataSource(
    private val settingsQueries: SettingsQueries
) : DataSourceContract.SettingsDataSource {

    override fun getSettings(dispatcher: CoroutineDispatcher): Flow<Result<SettingsEntity>> {
        return settingsQueries.get()
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::mapToEntity)
            .catch { emit(Result.failure(it)) }
    }

    private fun mapToEntity(settings: DTO?): Result<SettingsEntity> {
        return settings
            ?.let { Result.success(toSettingsEntity(it)) }
            ?: Result.failure(NoSuchElementException())
    }

    override suspend fun insert(settings: SettingsEntity) {
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

    private fun toSettingsEntity(settings: DTO): SettingsEntity =
        SettingsEntity(
            settings.timeOut,
            settings.snooze,
            settings.theme,
        )
}
