package com.cristianomadeira.ulessontest.data.dataSource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cristianomadeira.ulessontest.data.dataSource.local.dao.BookmarkDao
import com.cristianomadeira.ulessontest.data.dataSource.local.dao.ChapterDao
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.BookmarkEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.ChapterEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.LessonEntity

@Database(
    entities = [ChapterEntity::class, LessonEntity::class, BookmarkEntity::class],
    version = 1
)
abstract class UlessonDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
    abstract fun bookmarkDao(): BookmarkDao
}