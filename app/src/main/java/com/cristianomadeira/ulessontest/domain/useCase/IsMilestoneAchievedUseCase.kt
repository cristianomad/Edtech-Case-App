package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsMilestoneAchievedUseCase @Inject constructor(
    private val repository: MilestoneRepository
) {
    suspend operator fun invoke(): Flow<Boolean> =
        repository.isMilestoneAchieved()
}