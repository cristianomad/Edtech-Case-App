package com.cristianomadeira.ulessontest.data.repository

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.dataSource.local.MilestoneDataSource
import com.cristianomadeira.ulessontest.data.dataSource.local.entity.MilestoneEntity
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
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.junit.Before
import org.junit.Test

class MilestoneRepositoryTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var milestoneDataSource: MilestoneDataSource

    private lateinit var milestoneRepository: MilestoneRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
        milestoneRepository = MilestoneRepositoryImpl(
            milestoneDataSource,
            testDispatcher
        )
    }

    @Test
    fun `test milestone not seen`() {
        val fixture = MilestoneEntity(fixture(), false)

        coEvery { milestoneDataSource.getMilestone() } returns  flowOf(fixture)

        testScope.runTest {
            val isMilestoneAchieved = milestoneRepository.isMilestoneAchieved().first()

            coVerify { milestoneDataSource.getMilestone() }
            assertThat(isMilestoneAchieved).isTrue()
        }
    }

    @Test
    fun `test milestone seen`() {
        val fixture = MilestoneEntity(fixture(), true)

        coEvery { milestoneDataSource.getMilestone() } returns flowOf(fixture)

        testScope.runTest {
            val isMilestoneAchieved = milestoneRepository.isMilestoneAchieved().first()

            coVerify { milestoneDataSource.getMilestone() }
            assertThat(isMilestoneAchieved).isFalse()
        }
    }

    @Test
    fun `test set milestone as seen`() {
        val fixture = MilestoneEntity(fixture(), true)

        coEvery { milestoneDataSource.getMilestone() } returns flowOf(fixture)
        coEvery { milestoneDataSource.setMilestone(any()) } returns Unit

        testScope.runTest {
            milestoneRepository.setMilestoneAsSeen()
            coVerify { milestoneDataSource.getMilestone() }
            coVerify { milestoneDataSource.setMilestone(fixture) }
        }
    }

    @Test
    fun `test set new milestone when is a new day`() {
        val yesterdayDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(period = DatePeriod(days = 1))
        val fixture = MilestoneEntity(yesterdayDate.toString(), false)

        coEvery { milestoneDataSource.getMilestone() } returns flowOf(fixture)
        coEvery { milestoneDataSource.setMilestone(any()) } returns Unit

        testScope.runTest {
            milestoneRepository.setNewMilestone()
            coVerify { milestoneDataSource.getMilestone() }
            coVerify { milestoneDataSource.setMilestone(any()) }
        }
    }

    @Test
    fun `test set new milestone when no milestone was set`() {
        coEvery { milestoneDataSource.getMilestone() } returns flowOf(null)
        coEvery { milestoneDataSource.setMilestone(any()) } returns Unit

        testScope.runTest {
            milestoneRepository.setNewMilestone()
            coVerify { milestoneDataSource.getMilestone() }
            coVerify { milestoneDataSource.setMilestone(any()) }
        }
    }

    @Test
    fun `test don't set new milestone when there is already a milestone set for the same day`() {
        val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val fixture = MilestoneEntity(todayDate.toString(), false)

        coEvery { milestoneDataSource.getMilestone() } returns flowOf(fixture)
        coEvery { milestoneDataSource.setMilestone(any()) } returns Unit

        testScope.runTest {
            milestoneRepository.setNewMilestone()
            coVerify { milestoneDataSource.getMilestone() }
            coVerify(exactly = 0) { milestoneDataSource.setMilestone(any()) }
        }
    }
}