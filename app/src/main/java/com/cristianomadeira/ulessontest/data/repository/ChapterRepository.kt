package com.cristianomadeira.ulessontest.data.repository

import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {
    suspend fun getChapters(): Flow<Result<List<Chapter>>>

    suspend fun getLesson(id: Int): Lesson

}