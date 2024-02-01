package org.a_cyb.sayitalarm.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.a_cyb.sayitalarm.core.data.repository.AlarmRepository
import org.a_cyb.sayitalarm.core.data.repository.ImplAlarmRepository
import org.a_cyb.sayitalarm.core.data.repository.ImplUserDataRepository
import org.a_cyb.sayitalarm.core.data.repository.UserDataRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsAlarmRepository(
        alarmRepository: ImplAlarmRepository
    ): AlarmRepository

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: ImplUserDataRepository
    ): UserDataRepository
}