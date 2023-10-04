package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.ChapterRepository
import com.cristianomadeira.ulessontest.domain.model.Lesson
import javax.inject.Inject

class GetLessonUseCase @Inject constructor(
    private val repository: ChapterRepository
) {
    suspend operator fun invoke(id: Int): Lesson =
        repository.getLesson(id)
}