package com.cristianomadeira.ulessontest.domain.useCase

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.repository.WatchProgressRepository
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
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

class WatchProgressUseCaseTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var watchProgressRepository: WatchProgressRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test repository get watch progress is called`() {
        val getWatchProgressUseCase = GetWatchProgressUseCase(watchProgressRepository)

        coEvery { watchProgressRepository.getWatchProgress() } returns flowOf(fixture<WatchProgress>())

        testScope.runTest {
            getWatchProgressUseCase()
            coVerify { watchProgressRepository.getWatchProgress() }
        }
    }

    @Test
    fun `test repository set watch progress is called`() {
        val setWatchProgressUseCase = SetWatchProgressUseCase(watchProgressRepository)

        coEvery { watchProgressRepository.setWatchProgress(any()) } returns Unit

        testScope.runTest {
            val watchProgress = fixture<WatchProgress>()
            setWatchProgressUseCase(watchProgress)
            coVerify { watchProgressRepository.setWatchProgress(watchProgress) }
        }
    }

    @Test
    fun `test repository clear watch progress is called`() {
        val clearWatchProgressUseCase = ClearWatchProgressUseCase(watchProgressRepository)

        coEvery { watchProgressRepository.clearWatchProgress() } returns Unit

        testScope.runTest {
            clearWatchProgressUseCase()
            coVerify { watchProgressRepository.clearWatchProgress() }
        }
    }
}