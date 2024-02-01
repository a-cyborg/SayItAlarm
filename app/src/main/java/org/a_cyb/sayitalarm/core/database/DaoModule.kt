package org.a_cyb.sayitalarm.core.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.a_cyb.sayitalarm.core.database.dao.AlarmDao
import org.a_cyb.sayitalarm.core.database.dao.AlarmInstanceDao

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
   @Provides
   fun providesAlarmDao(
       database: SiaDatabase,
   ): AlarmDao = database.alarmDao()

    @Provides
    fun provideAlarmInstanceDao(
        database: SiaDatabase,
    ): AlarmInstanceDao = database.alarmInstanceDao()
}