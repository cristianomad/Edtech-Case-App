package com.cristianomadeira.ulessontest.data.dataSource.local

import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import kotlinx.coroutines.flow.Flow

interface ChapterDataSource {

    suspend fun getChapters(): Flow<List<Chapter>>

    suspend fun insertChapters(chapters: List<Chapter>)

    suspend fun removeChapters()

    suspend fun getLesson(id: Int): Lesson
}