package com.cristianomadeira.ulessontest.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark WHERE lessonId = :lessonId")
    fun getBookmarks(lessonId: Int): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmark WHERE id = :id")
    fun deleteBookmark(id: Int)
}