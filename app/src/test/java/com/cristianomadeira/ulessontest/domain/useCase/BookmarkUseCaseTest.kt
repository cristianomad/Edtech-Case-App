package com.cristianomadeira.ulessontest.domain.useCase

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.repository.BookmarkRepository
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

class BookmarkUseCaseTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var bookmarkRepository: BookmarkRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test repository get bookmark is called`() {
        val getBookmarkUseCase = GetBookmarksUseCase(bookmarkRepository)

        coEvery { bookmarkRepository.getBookmarks(any()) } returns flowOf(listOf(fixture<Bookmark>()))

        testScope.runTest {
            val lessonId = fixture<Int>()
            getBookmarkUseCase(lessonId)
            coVerify { bookmarkRepository.getBookmarks(lessonId) }
        }
    }

    @Test
    fun `test repository delete bookmark is called`() {
        val deleteBookmarkUseCase = DeleteBookmarkUseCase(bookmarkRepository)

        coEvery { bookmarkRepository.deleteBookmark(any()) } returns Unit

        testScope.runTest {
            val id = fixture<Int>()
            deleteBookmarkUseCase(id)
            coVerify { bookmarkRepository.deleteBookmark(id) }
        }
    }

    @Test
    fun `test repository add bookmark is called`() {
        val addBookmarkUseCase = AddBookmarkUseCase(bookmarkRepository)

        coEvery { bookmarkRepository.addBookmark(any()) } returns Unit

        testScope.runTest {
            val bookmark = fixture<Bookmark>()
            addBookmarkUseCase(bookmark)
            coVerify { bookmarkRepository.addBookmark(bookmark) }
        }
    }
}