package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.MilestoneRepository
import javax.inject.Inject

class SetNewMilestoneUseCase @Inject constructor(
    private val repository: MilestoneRepository
) {
    suspend operator fun invoke() =
        repository.setNewMilestone()
}