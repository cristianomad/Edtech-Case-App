package com.cristianomadeira.ulessontest.data.dataSource.local

import com.cristianomadeira.ulessontest.data.dataSource.local.entity.MilestoneEntity
import kotlinx.coroutines.flow.Flow

interface MilestoneDataSource {
    suspend fun getMilestone() : Flow<MilestoneEntity?>

    suspend fun setMilestone(milestone: MilestoneEntity)
}