package com.cristianomadeira.ulessontest.data.repository

import com.cristianomadeira.ulessontest.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun getBookmarks(lessonId: Int): Flow<List<Bookmark>>

    suspend fun addBookmark(bookmark: Bookmark)

    suspend fun deleteBookmark(id: Int)
}