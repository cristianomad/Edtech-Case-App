package com.cristianomadeira.ulessontest.data.dataSource.local

import com.cristianomadeira.ulessontest.data.dataSource.local.database.UlessonDatabase
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.toEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.toModel
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChapterDataSourceImpl @Inject constructor(
    private val database: UlessonDatabase
) : ChapterDataSource {

    override suspend fun getChapters(): Flow<List<Chapter>> =
        database.chapterDao().getChapters().map { chapterWithLessons ->
            chapterWithLessons.map { it.toModel() }
        }

    override suspend fun insertChapters(chapters: List<Chapter>) =
        database.chapterDao().insertChaptersWithLessons(
            chapters.map {
                it.toEntity()
            }
        )

    override suspend fun removeChapters() =
        database.chapterDao().removeChapters()

    override suspend fun getLesson(id: Int): Lesson =
        database.chapterDao().getLesson(id).toModel()
}