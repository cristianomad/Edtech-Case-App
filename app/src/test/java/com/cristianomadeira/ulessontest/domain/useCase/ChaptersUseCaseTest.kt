package com.cristianomadeira.ulessontest.domain.useCase

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.repository.ChapterRepository
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
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

class ChaptersUseCaseTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var chapterRepository: ChapterRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test repository get chapters is called`() {
        val getChaptersUseCase = GetChaptersUseCase(chapterRepository)

        coEvery { chapterRepository.getChapters() } returns flowOf(fixture<Result<List<Chapter>>>())

        testScope.runTest {
            getChaptersUseCase()
            coVerify { chapterRepository.getChapters() }
        }
    }

    @Test
    fun `test repository get lesson is called`() {
        val getLessonUseCase = GetLessonUseCase(chapterRepository)

        coEvery { chapterRepository.getLesson(any()) } returns fixture<Lesson>()

        testScope.runTest {
            val id = fixture<Int>()
            getLessonUseCase(id)
            coVerify { chapterRepository.getLesson(id) }
        }
    }
}