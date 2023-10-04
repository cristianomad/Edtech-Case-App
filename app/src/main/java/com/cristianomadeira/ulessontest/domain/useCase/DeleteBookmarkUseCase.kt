package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.BookmarkRepository
import javax.inject.Inject

class DeleteBookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(lessonId: Int) =
        repository.deleteBookmark(lessonId)
}