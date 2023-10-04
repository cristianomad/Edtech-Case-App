package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.BookmarkRepository
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(lessonId: Int): Flow<List<Bookmark>> =
        repository.getBookmarks(lessonId)
}