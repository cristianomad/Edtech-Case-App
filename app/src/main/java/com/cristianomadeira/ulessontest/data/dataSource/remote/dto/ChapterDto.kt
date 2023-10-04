package com.cristianomadeira.ulessontest.data.dataSource.remote.dto

import com.cristianomadeira.ulessontest.domain.model.Chapter
import kotlinx.serialization.Serializable

@Serializable
data class ChapterDto(
    val id: Int,
    val enumeration: Int,
    val title: String,
    val lessons: List<LessonDto>
)

fun ChapterDto.toModel(): Chapter =
    Chapter(
        id = id,
        enumeration = enumeration,
        title = title,
        lessons = lessons.map { it.toModel() }
    )