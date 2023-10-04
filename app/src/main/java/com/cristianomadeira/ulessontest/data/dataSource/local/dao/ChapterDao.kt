package com.cristianomadeira.ulessontest.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.ChapterEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.ChapterWithLessonsEntity
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Transaction
    @Query("SELECT * FROM chapter")
    fun getChapters(): Flow<List<ChapterWithLessonsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChapter(chapters: ChapterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLessons(chapters: List<LessonEntity>)

    @Transaction
    fun insertChaptersWithLessons(chaptersWithLessons: List<ChapterWithLessonsEntity>) {
        chaptersWithLessons.forEach {
            insertChapter(it.chapter)
            insertLessons(it.lessons)
        }
    }

    @Query("DELETE FROM lesson")
    fun removeLessons()

    @Query("DELETE FROM chapter")
    fun removeChapters()

    @Transaction
    fun removeChaptersAndLessons() {
        removeLessons()
        removeChapters()
    }

    @Query("SELECT * FROM lesson WHERE id = :id")
    fun getLesson(id: Int): LessonEntity
}