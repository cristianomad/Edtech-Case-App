package com.cristianomadeira.ulessontest.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cristianomadeira.ulessontest.domain.model.Bookmark

@Entity(tableName = "bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lessonId: Int,
    val content: String,
    val position: Long
)

fun BookmarkEntity.toModel() = Bookmark(
    id = id,
    lessonId = lessonId,
    content = content,
    position = position
)

fun Bookmark.toEntity() = BookmarkEntity(
    lessonId = lessonId,
    content = content,
    position = position
)