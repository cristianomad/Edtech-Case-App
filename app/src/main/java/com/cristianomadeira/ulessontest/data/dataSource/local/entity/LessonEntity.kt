package com.cristianomadeira.ulessontest.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cristianomadeira.ulessontest.domain.model.Lesson

@Entity(tableName = "lesson")
class LessonEntity(
    @PrimaryKey val id: Int,
    val chapterId: Int,
    val title: String,
    val thumbUrl: String,
    val videoUrl: String
)

fun LessonEntity.toModel() = Lesson(
    id = id,
    title = title,
    thumbUrl = thumbUrl,
    videoUrl = videoUrl
)

fun Lesson.toEntity(chapterId: Int) = LessonEntity(
    id = id,
    chapterId = chapterId,
    title = title,
    thumbUrl = thumbUrl,
    videoUrl = videoUrl
)