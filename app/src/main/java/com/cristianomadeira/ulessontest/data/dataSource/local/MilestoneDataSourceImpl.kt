package com.cristianomadeira.ulessontest.data.dataSource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.MilestoneEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val MILESTONE_KEY = "milestone_key"

class MilestoneDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : MilestoneDataSource {
    override suspend fun getMilestone(): Flow<MilestoneEntity?> =
        dataStore.data.map {
            it[stringPreferencesKey(MILESTONE_KEY)] ?: ""
        }.map { json ->
            if (json.isEmpty()) return@map null
            Json.decodeFromString(json)
        }

    override suspend fun setMilestone(milestone: MilestoneEntity) {
        dataStore.edit {
            it[stringPreferencesKey(MILESTONE_KEY)] = Json.encodeToString(milestone)
        }
    }
}