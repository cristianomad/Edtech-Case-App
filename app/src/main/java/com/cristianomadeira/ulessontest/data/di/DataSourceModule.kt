package com.cristianomadeira.ulessontest.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.cristianomadeira.ulessontest.data.dataSource.local.database.UlessonDatabase
import com.cristianomadeira.ulessontest.data.dataSource.remote.client.HttpClientProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient = HttpClientProvider(CIO.create())

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): UlessonDatabase =
        Room.databaseBuilder(
            context,
            UlessonDatabase::class.java, "ulesson"
        ).build()

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}