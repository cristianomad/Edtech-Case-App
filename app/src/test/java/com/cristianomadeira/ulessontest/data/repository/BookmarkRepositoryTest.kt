package com.cristianomadeira.ulessontest.data.repository

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.dataSource.local.BookmarkDataSource
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class BookmarkRepositoryTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var bookmarkDataSource: BookmarkDataSource

    private lateinit var bookmarkRepository: BookmarkRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
        bookmarkRepository = BookmarkRepositoryImpl(
            bookmarkDataSource,
            testDispatcher
        )
    }

    @Test
    fun `test data source get bookmark is called`() {
        coEvery { bookmarkDataSource.getBookmarks(any()) } returns flowOf(listOf(fixture<Bookmark>()))

        testScope.runTest {
            val lessonId = fixture<Int>()
            bookmarkRepository.getBookmarks(lessonId)
            coVerify { bookmarkDataSource.getBookmarks(lessonId) }
        }
    }

    @Test
    fun `test data source insert bookmark is called`() {
        coEvery { bookmarkDataSource.insertBookmark(any()) } returns Unit


        testScope.runTest {
            val bookmark = fixture<Bookmark>()
            bookmarkRepository.addBookmark(bookmark)
            coVerify { bookmarkDataSource.insertBookmark(bookmark) }
        }
    }

    @Test
    fun `test data source delete bookmark is called`() {
        coEvery { bookmarkDataSource.deleteBookmark(any()) } returns Unit

        testScope.runTest {
            val bookmarkId = fixture<Int>()
            bookmarkRepository.deleteBookmark(bookmarkId)
            coVerify { bookmarkDataSource.deleteBookmark(bookmarkId) }
        }
    }
}