package com.cristianomadeira.ulessontest.data.dataSource.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.cristianomadeira.ulessontest.domain.model.Chapter

@Entity(tableName = "chapter")
data class ChapterEntity(
    @PrimaryKey val id: Int,
    val enumeration: Int,
    val title: String
)

data class ChapterWithLessonsEntity(
    @Embedded val chapter: ChapterEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "chapterId"
    )
    val lessons: List<LessonEntity>
)

fun ChapterWithLessonsEntity.toModel() = Chapter(
    id = chapter.id,
    enumeration = chapter.enumeration,
    title = chapter.title,
    lessons = lessons.map { it.toModel() }
)

fun Chapter.toEntity() = ChapterWithLessonsEntity(
    chapter = ChapterEntity(
        id = id,
        enumeration = enumeration,
        title = title
    ),
    lessons = lessons.map { it.toEntity(id) }
)
