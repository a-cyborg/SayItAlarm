/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.data.datasource.DataSourceContract
import org.a_cyb.sayitalarm.data.model.toSettings
import org.a_cyb.sayitalarm.data.model.toSettingsDTO
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Settings
import org.a_cyb.sayitalarm.entity.Snooze
import org.a_cyb.sayitalarm.entity.Theme
import org.a_cyb.sayitalarm.entity.TimeOut
import org.acyb.sayitalarm.database.Get as SettingsDTO

class SettingsRepository(
    private val dataSource: DataSourceContract.SettingsDataSource,
    private val dispatcher: CoroutineDispatcher,
) : RepositoryContract.SettingsRepository {

    override fun getSettings(): Flow<Result<Settings>> {
        return dataSource
            .getSettings(dispatcher)
            .map(::map)
    }

    private fun map(result: Result<SettingsDTO>): Result<Settings> {
        return result.map { it.toSettings() }
    }

    override fun insertOrIgnore(settings: Settings, scope: CoroutineScope) {
        scope.launch(dispatcher) {
            dataSource.insert(settings.toSettingsDTO())
        }
    }

    override fun setTimeOut(timeOut: TimeOut, scope: CoroutineScope) {
        scope.launch(context = dispatcher) {
            dataSource.setTimeOut(timeOut.timeOut.toLong())
        }
    }

    override fun setSnooze(snooze: Snooze, scope: CoroutineScope) {
        scope.launch(context = dispatcher) {
            dataSource.setSnooze(snooze.snooze.toLong())
        }
    }

    override fun setTheme(theme: Theme, scope: CoroutineScope) {
        scope.launch(context = dispatcher) {
            dataSource.setTheme(theme.ordinal.toLong())
        }
    }
}
