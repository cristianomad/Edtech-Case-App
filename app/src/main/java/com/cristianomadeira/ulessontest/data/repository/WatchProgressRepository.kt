package com.cristianomadeira.ulessontest.data.repository

import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import kotlinx.coroutines.flow.Flow

interface WatchProgressRepository {
    suspend fun getWatchProgress(): Flow<WatchProgress?>

    suspend fun setWatchProgress(watchProgress: WatchProgress)

    suspend fun clearWatchProgress()
}