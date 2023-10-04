package com.cristianomadeira.ulessontest.data.dataSource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cristianomadeira.ulessontest.data.di.dataStore
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import javax.inject.Inject

private const val WATCH_PROGRESS_KEY = "watch_progress_key"
class WatchProgressDataSourceImpl @Inject constructor(
    private val context: Context
) : WatchProgressDataSource {
    override suspend fun getWatchProgress(): Flow<WatchProgress?> =
        context.dataStore.data.map {
            it[stringPreferencesKey(WATCH_PROGRESS_KEY)] ?: ""
        }.map { jsonString ->
            if (jsonString.isEmpty()) null else decodeFromString(jsonString)
        }

    override suspend fun setWatchProgress(watchProgress: WatchProgress) {
        context.dataStore.edit {
            it[stringPreferencesKey(WATCH_PROGRESS_KEY)] = Json.encodeToString(watchProgress)
        }
    }

    override suspend fun clearWatchProgress() {
        context.dataStore.edit {
            it.remove(stringPreferencesKey(WATCH_PROGRESS_KEY))
        }
    }
}