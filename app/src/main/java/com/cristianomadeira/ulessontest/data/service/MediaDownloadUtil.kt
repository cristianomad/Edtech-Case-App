package com.cristianomadeira.ulessontest.data.service

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource.Factory
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.Executors

/**
 * Utility methods for the media downloads.
 * Adapted from: https://github.com/androidx/media/blob/release/demos/main/src/main/java/androidx/media3/demo/main/DemoUtil.java
 */
@UnstableApi
object MediaDownloadUtil {

    const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_chanel"
    private const val DOWNLOAD_CONTENT_DIR = "downloads"

    private lateinit var dataSourceFactory: Factory
    private lateinit var httpDataSourceFactory: Factory
    private lateinit var databaseProvider: DatabaseProvider
    private lateinit var downloadCache: Cache
    private lateinit var downloadManager: DownloadManager
    private lateinit var cookieManager: CookieManager
    private lateinit var downloadNotificationHelper: DownloadNotificationHelper

    fun getDataSourceFactory(context: Context): Factory {
        if (!MediaDownloadUtil::dataSourceFactory.isInitialized) {
            val upstreamFactory = DefaultDataSource.Factory(context, getHttpDataSourceFactory())
            dataSourceFactory = buildReadOnlyCacheDataSource(
                upstreamFactory,
                getDownloadCache(context)
            )
        }

        return dataSourceFactory
    }

    fun getDownloadManager(context: Context): DownloadManager {
        if (!MediaDownloadUtil::downloadManager.isInitialized) {
            downloadManager = DownloadManager(
                context,
                getDatabaseProvider(context),
                getDownloadCache(context),
                getHttpDataSourceFactory(),
                Executors.newFixedThreadPool(6)
            )
        }

        return downloadManager
    }

    fun getDownloadNotificationHelper(context: Context): DownloadNotificationHelper {
        if (!MediaDownloadUtil::downloadNotificationHelper.isInitialized) {
            downloadNotificationHelper = DownloadNotificationHelper(
                context,
                DOWNLOAD_NOTIFICATION_CHANNEL_ID
            )
        }

        return downloadNotificationHelper
    }

    fun getDownloadCache(context: Context): Cache {
        if (!MediaDownloadUtil::downloadCache.isInitialized) {
            val downloadContentDirectory = File(context.filesDir, DOWNLOAD_CONTENT_DIR)

            if (!downloadContentDirectory.exists()) {
                downloadContentDirectory.mkdir()
            }

            downloadCache = SimpleCache(
                downloadContentDirectory,
                NoOpCacheEvictor(),
                getDatabaseProvider(context)
            )
        }

        return downloadCache
    }

    private fun getHttpDataSourceFactory(): Factory {
        if (!MediaDownloadUtil::httpDataSourceFactory.isInitialized) {
            cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
            CookieHandler.setDefault(cookieManager)
            httpDataSourceFactory = DefaultHttpDataSource.Factory()
        }

        return httpDataSourceFactory
    }

    private fun getDatabaseProvider(context: Context): DatabaseProvider {
        if (!MediaDownloadUtil::databaseProvider.isInitialized) {
            databaseProvider = StandaloneDatabaseProvider(context)
        }

        return databaseProvider
    }

    private fun buildReadOnlyCacheDataSource(
        upstreamFactory: Factory,
        cache: Cache
    ): Factory = CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(upstreamFactory)
        .setCacheWriteDataSinkFactory(null)
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
}