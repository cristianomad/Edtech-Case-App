package com.cristianomadeira.ulessontest.domain.useCase

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.repository.MilestoneRepository
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

class MilestoneUseCaseTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @MockK
    lateinit var milestoneRepository: MilestoneRepository

    @Before
    fun prepare() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test repository is milestone achieved called`() {
        val isMilestoneAchievedUseCase = IsMilestoneAchievedUseCase(milestoneRepository)

        coEvery { milestoneRepository.isMilestoneAchieved() } returns flowOf(fixture<Boolean>())

        testScope.runTest {
            isMilestoneAchievedUseCase()
            coVerify { milestoneRepository.isMilestoneAchieved() }
        }
    }

    @Test
    fun `test repository set milestone called`() {
        val setNewMilestoneUseCase = SetNewMilestoneUseCase(milestoneRepository)

        coEvery { milestoneRepository.setNewMilestone() } returns Unit

        testScope.runTest {
            setNewMilestoneUseCase()
            coVerify { milestoneRepository.setNewMilestone() }
        }
    }

    @Test
    fun `test repository set milestone as seen called`() {
        val setMilestoneAsSeenUseCase = SetMilestoneAsSeenUseCase(milestoneRepository)

        coEvery { milestoneRepository.setMilestoneAsSeen() } returns Unit

        testScope.runTest {
            setMilestoneAsSeenUseCase()
            coVerify { milestoneRepository.setMilestoneAsSeen() }
        }
    }
}