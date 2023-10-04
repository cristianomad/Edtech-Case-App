package com.cristianomadeira.ulessontest.data.repository

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.dataSource.local.WatchProgressDataSource
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

class WatchProgressRepositoryTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var watchProgressDataSource: WatchProgressDataSource

    private lateinit var watchProgressRepository: WatchProgressRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
        watchProgressRepository =
            WatchProgressRepositoryImpl(
                watchProgressDataSource,
                testDispatcher
            )
    }

    @Test
    fun `test data source get watch progress is called`() {
        coEvery { watchProgressDataSource.getWatchProgress() } returns flowOf(fixture<WatchProgress?>())

        testScope.runTest {
            watchProgressRepository.getWatchProgress()
            coVerify { watchProgressDataSource.getWatchProgress() }
        }
    }

    @Test
    fun `test data source set watch progress is called`() {
        coEvery { watchProgressDataSource.setWatchProgress(any()) } returns Unit

        testScope.runTest {
            val watchProgress = fixture<WatchProgress>()
            watchProgressRepository.setWatchProgress(watchProgress)
            coVerify { watchProgressDataSource.setWatchProgress(watchProgress) }
        }
    }

    @Test
    fun `test data source clear watch progress is called`() {
        coEvery { watchProgressDataSource.clearWatchProgress() } returns Unit

        testScope.runTest {
            watchProgressRepository.clearWatchProgress()
            coVerify { watchProgressDataSource.clearWatchProgress() }
        }
    }
}