package com.cristianomadeira.ulessontest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val id: Int,
    val title: String,
    val thumbUrl: String,
    val videoUrl: String
)
