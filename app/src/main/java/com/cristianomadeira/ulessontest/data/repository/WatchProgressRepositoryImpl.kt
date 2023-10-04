package com.cristianomadeira.ulessontest.data.repository

import com.cristianomadeira.ulessontest.data.dataSource.local.WatchProgressDataSource
import com.cristianomadeira.ulessontest.data.di.CoroutinesDispatchers
import com.cristianomadeira.ulessontest.data.di.Dispatcher
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WatchProgressRepositoryImpl @Inject constructor(
    private val dataSource: WatchProgressDataSource,
    @Dispatcher(CoroutinesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
): WatchProgressRepository {
    override suspend fun getWatchProgress(): Flow<WatchProgress?> = withContext(ioDispatcher) {
        dataSource.getWatchProgress()
    }

    override suspend fun setWatchProgress(watchProgress: WatchProgress) = withContext(ioDispatcher) {
        dataSource.setWatchProgress(watchProgress)
    }

    override suspend fun clearWatchProgress() = withContext(ioDispatcher) {
        dataSource.clearWatchProgress()
    }
}