package com.cristianomadeira.ulessontest.ui.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.cristianomadeira.ulessontest.data.service.MediaDownloadManagerImpl
import com.cristianomadeira.ulessontest.data.service.MediaDownloadManager
import com.cristianomadeira.ulessontest.data.service.MediaDownloadUtil
import com.cristianomadeira.ulessontest.ui.util.ConnectivityUtil
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@UnstableApi
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun providesDownloadCache(@ApplicationContext context: Context) =
        MediaDownloadUtil.getDownloadCache(context)

    @Provides
    @Singleton
    fun providesConnectivityUtil(@ApplicationContext context: Context) =
        ConnectivityUtil(context)
}

@UnstableApi
@Module
@InstallIn(SingletonComponent::class)
interface ApplicationBindModule {

    @Binds
    fun bindsMediaDownloadManager(
        mediaDownloadManager: MediaDownloadManagerImpl
    ): MediaDownloadManager
}