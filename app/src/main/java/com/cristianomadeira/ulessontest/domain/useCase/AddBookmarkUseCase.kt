package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.BookmarkRepository
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(bookmark: Bookmark) =
        repository.addBookmark(bookmark)
}