package com.cristianomadeira.ulessontest.data.dataSource.local

import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import kotlinx.coroutines.flow.Flow

interface WatchProgressDataSource {
    suspend fun getWatchProgress(): Flow<WatchProgress?>

    suspend fun setWatchProgress(watchProgress: WatchProgress)

    suspend fun clearWatchProgress()
}