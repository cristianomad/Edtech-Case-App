package com.cristianomadeira.ulessontest.data.repository

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.ChapterDto
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.cristianomadeira.ulessontest.data.dataSource.local.ChapterDataSource as LocalChapterDataSource
import com.cristianomadeira.ulessontest.data.dataSource.remote.ChapterDataSource as RemoteChapterDataSource

class ChapterRepositoryTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var chapterRemoteDataSource: RemoteChapterDataSource

    @MockK
    lateinit var chapterLocalDataSource: LocalChapterDataSource

    private lateinit var chapterRepository: ChapterRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
        chapterRepository = OfflineFirstChapterRepository(
            chapterRemoteDataSource,
            chapterLocalDataSource,
            testDispatcher
        )
    }

    @Test
    fun `test get chapters successfully from local and remote data source`() {
        coEvery { chapterRemoteDataSource.fetchChapters() } returns Result.success(fixture<List<ChapterDto>>())
        coEvery { chapterLocalDataSource.getChapters() } returns flowOf(fixture<List<Chapter>>())
        coEvery { chapterLocalDataSource.insertChapters(any()) } returns Unit
        coEvery { chapterLocalDataSource.removeChapters() } returns Unit

        testScope.runTest {
            val result = chapterRepository.getChapters()
            coVerify { chapterLocalDataSource.getChapters() }
            coVerify { chapterRemoteDataSource.fetchChapters() }
            assertThat(result.first().isSuccess).isTrue()
        }
    }

    @Test
    fun `test get chapters unsuccessfully from local and remote data source`() {
        coEvery { chapterRemoteDataSource.fetchChapters() } returns Result.failure(Exception())
        coEvery { chapterLocalDataSource.getChapters() } returns flowOf(listOf())
        coEvery { chapterLocalDataSource.insertChapters(any()) } returns Unit
        coEvery { chapterLocalDataSource.removeChapters() } returns Unit

        testScope.runTest {
            val result = chapterRepository.getChapters()
            coVerify { chapterLocalDataSource.getChapters() }
            coVerify { chapterRemoteDataSource.fetchChapters() }
            assertThat(result.first().isFailure).isTrue()
        }
    }

    @Test
    fun `test local data source get lesson is called`() {
        coEvery { chapterLocalDataSource.getLesson(any()) } returns fixture<Lesson>()

        testScope.runTest {
            val lessonId = fixture<Int>()
            chapterRepository.getLesson(lessonId)
            coVerify { chapterLocalDataSource.getLesson(lessonId) }
        }
    }
}