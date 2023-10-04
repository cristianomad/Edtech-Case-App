package com.cristianomadeira.ulessontest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WatchProgress(
    val lessonId: Int,
    val progress: Long,
    val lesson: Lesson? = null
)