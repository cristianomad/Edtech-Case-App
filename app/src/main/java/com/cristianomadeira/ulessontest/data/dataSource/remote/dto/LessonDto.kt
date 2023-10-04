package com.cristianomadeira.ulessontest.data.dataSource.remote.dto

import com.cristianomadeira.ulessontest.domain.model.Lesson
import kotlinx.serialization.Serializable

@Serializable
data class LessonDto(
    val id: Int,
    val title: String,
    val thumbUrl: String,
    val videoUrl: String
)

fun LessonDto.toModel(): Lesson =
    Lesson(
        id = id,
        title = title,
        thumbUrl = thumbUrl,
        videoUrl = videoUrl
    )