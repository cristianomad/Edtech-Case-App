package com.cristianomadeira.ulessontest.data.repository

import kotlinx.coroutines.flow.Flow

interface MilestoneRepository {
    suspend fun isMilestoneAchieved() : Flow<Boolean>

    suspend fun setMilestoneAsSeen()

    suspend fun setNewMilestone()
}