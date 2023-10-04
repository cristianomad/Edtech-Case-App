package com.cristianomadeira.ulessontest.data.repository

import com.cristianomadeira.ulessontest.data.dataSource.local.BookmarkDataSource
import com.cristianomadeira.ulessontest.data.di.CoroutinesDispatchers.IO
import com.cristianomadeira.ulessontest.data.di.Dispatcher
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val dataSource: BookmarkDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
): BookmarkRepository {
    override suspend fun getBookmarks(lessonId: Int): Flow<List<Bookmark>> = withContext(ioDispatcher) {
        dataSource.getBookmarks(lessonId)
    }

    override suspend fun addBookmark(bookmark: Bookmark) = withContext(ioDispatcher) {
        dataSource.insertBookmark(bookmark)
    }

    override suspend fun deleteBookmark(id: Int) = withContext(ioDispatcher) {
        dataSource.deleteBookmark(id)
    }
}