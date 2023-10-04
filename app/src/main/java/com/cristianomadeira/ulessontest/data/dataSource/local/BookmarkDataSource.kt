package com.cristianomadeira.ulessontest.data.dataSource.local

import com.cristianomadeira.ulessontest.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkDataSource {

    suspend fun getBookmarks(lessonId: Int): Flow<List<Bookmark>>

    suspend fun insertBookmark(bookmark: Bookmark)

    suspend fun deleteBookmark(id: Int)
}