package org.a_cyb.sayitalarm.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.a_cyb.sayitalarm.UserPreferences
import org.a_cyb.sayitalarm.core.datastore.UserPreferencesSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        userPreferencesSerializer: UserPreferencesSerializer,
    ) : DataStore<UserPreferences>  =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            corruptionHandler = null,
            produceFile =  { context.dataStoreFile("user_preferences.pb") }
        )
}