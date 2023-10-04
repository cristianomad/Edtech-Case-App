package com.cristianomadeira.ulessontest.data.dataSource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cristianomadeira.ulessontest.data.dataSource.local.dao.BookmarkDao
import com.cristianomadeira.ulessontest.data.dataSource.local.database.UlessonDatabase
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookmarkDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var database: UlessonDatabase
    private lateinit var bookmarkDao: BookmarkDao
    private lateinit var bookmarkDataSource: BookmarkDataSource

    private val bookmark: Bookmark = Bookmark(
        id = 1,
        lessonId = 1,
        content = "test",
        position = 1000
    )

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UlessonDatabase::class.java
        ).allowMainThreadQueries().build()

        bookmarkDao = database.bookmarkDao()

        bookmarkDataSource = BookmarkDataSourceImpl(database)
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun test_insert_bookmark() {
        testScope.runTest {
            bookmarkDataSource.insertBookmark(bookmark)
            bookmarkDataSource.getBookmarks(bookmark.lessonId).first().let { bookmarks ->
                val subject = bookmarks.first()
                assertThat(subject.id).isEqualTo(bookmark.id)
                assertThat(subject.lessonId).isEqualTo(bookmark.lessonId)
                assertThat(subject.content).isEqualTo(bookmark.content)
                assertThat(subject.position).isEqualTo(bookmark.position)
            }
        }
    }

    @Test
    fun test_delete_bookmark() {
        testScope.runTest {
            bookmarkDataSource.insertBookmark(bookmark)
            bookmarkDataSource.deleteBookmark(bookmark.id)
            val bookmarks = bookmarkDataSource.getBookmarks(bookmark.lessonId).first()

            assertThat(bookmarks).isEmpty()
        }
    }
}