package com.cristianomadeira.ulessontest.data.dataSource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cristianomadeira.ulessontest.data.dataSource.local.dao.ChapterDao
import com.cristianomadeira.ulessontest.data.dataSource.local.database.UlessonDatabase
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChapterDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var database: UlessonDatabase
    private lateinit var chapterDao: ChapterDao
    private lateinit var chapterDataSource: ChapterDataSource
    private val chapter = Chapter(
        id = 1,
        enumeration = 1,
        title = "Chapter 1",
        lessons = listOf(
            Lesson(
                id = 1,
                title = "Lesson 1",
                thumbUrl = "image1.jpg",
                videoUrl = "video1.mp4"
            ),
            Lesson(
                id = 2,
                title = "Lesson 2",
                thumbUrl = "image2.jpg",
                videoUrl = "video2.mp4"
            )
        )
    )

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UlessonDatabase::class.java
        ).allowMainThreadQueries().build()

        chapterDao = database.chapterDao()
        chapterDataSource = ChapterDataSourceImpl(database)
    }

    @Test
    fun test_insert_chapter() {
        testScope.runTest {
            chapterDataSource.insertChapters(listOf(chapter))
            chapterDataSource.getChapters().first().let { chapters ->
                val subject = chapters.first()

                assertThat(subject.id).isEqualTo(chapter.id)
                assertThat(subject.enumeration).isEqualTo(chapter.enumeration)
                assertThat(subject.title).isEqualTo(chapter.title)

                subject.lessons.forEachIndexed { index, lesson ->
                    assertThat(lesson.id).isEqualTo(chapter.lessons[index].id)
                    assertThat(lesson.title).isEqualTo(chapter.lessons[index].title)
                    assertThat(lesson.thumbUrl).isEqualTo(chapter.lessons[index].thumbUrl)
                    assertThat(lesson.videoUrl).isEqualTo(chapter.lessons[index].videoUrl)
                }
            }
        }
    }

    @Test
    fun test_delete_chapters() {
        testScope.runTest {
            chapterDataSource.insertChapters(listOf(chapter))
            chapterDataSource.removeChapters()
            val chapters = chapterDataSource.getChapters().first()

            assertThat(chapters).isEmpty()
        }
    }

    @Test
    fun test_get_lesson() {
        testScope.runTest {
            chapterDataSource.insertChapters(listOf(chapter))
            val subjectLesson = chapterDataSource.getLesson(id = 1)
            val testLesson = chapter.lessons.first()

            assertThat(subjectLesson.id).isEqualTo(testLesson.id)
            assertThat(subjectLesson.title).isEqualTo(testLesson.title)
            assertThat(subjectLesson.thumbUrl).isEqualTo(testLesson.thumbUrl)
            assertThat(subjectLesson.videoUrl).isEqualTo(testLesson.videoUrl)
        }
    }
}