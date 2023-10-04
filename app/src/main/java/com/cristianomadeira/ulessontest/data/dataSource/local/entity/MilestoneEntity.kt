package com.cristianomadeira.ulessontest.data.dataSource.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class MilestoneEntity(
    val date: String,
    val isAchievementSeen: Boolean
)