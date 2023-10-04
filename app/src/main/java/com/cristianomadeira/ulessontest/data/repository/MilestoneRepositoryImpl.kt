package com.cristianomadeira.ulessontest.data.repository

import com.cristianomadeira.ulessontest.data.dataSource.local.MilestoneDataSource
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.MilestoneEntity
import com.cristianomadeira.ulessontest.data.di.CoroutinesDispatchers
import com.cristianomadeira.ulessontest.data.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.todayIn
import javax.inject.Inject

class MilestoneRepositoryImpl @Inject constructor(
    private val dataSource: MilestoneDataSource,
    @Dispatcher(CoroutinesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
): MilestoneRepository {
    override suspend fun isMilestoneAchieved(): Flow<Boolean> = withContext(ioDispatcher) {
        dataSource.getMilestone().map {
            it?.isAchievementSeen == false
        }
    }

    override suspend fun setMilestoneAsSeen(): Unit = withContext(ioDispatcher) {
        dataSource.getMilestone().firstOrNull()?.let { current ->
            dataSource.setMilestone(current.copy(isAchievementSeen = true))
        }
    }

    override suspend fun setNewMilestone(): Unit = withContext(ioDispatcher) {
        val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

        suspend fun setNewMilestone() {
            dataSource.setMilestone(
                MilestoneEntity(
                    date = todayDate.toString(),
                    isAchievementSeen = false
                )
            )
        }

        dataSource.getMilestone().firstOrNull()?.let { milestone ->
            if (todayDate > milestone.date.toLocalDate()) {
                setNewMilestone()
            }
        } ?: run {
            setNewMilestone()
        }
    }
}