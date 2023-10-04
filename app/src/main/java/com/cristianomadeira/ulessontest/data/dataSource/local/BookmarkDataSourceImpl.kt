package com.cristianomadeira.ulessontest.data.dataSource.local

import com.cristianomadeira.ulessontest.data.dataSource.local.database.UlessonDatabase
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.toEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.toModel
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkDataSourceImpl @Inject constructor(
    private val database: UlessonDatabase
): BookmarkDataSource {
    override suspend fun getBookmarks(lessonId: Int): Flow<List<Bookmark>> =
        database.bookmarkDao().getBookmarks(lessonId).map {
            it.map { bookmarkEntity ->
                bookmarkEntity.toModel()
            }
        }

    override suspend fun insertBookmark(bookmark: Bookmark) =
        database.bookmarkDao().insertBookmark(bookmark.toEntity())

    override suspend fun deleteBookmark(id: Int) =
        database.bookmarkDao().deleteBookmark(id)
}