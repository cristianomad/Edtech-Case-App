package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.WatchProgressRepository
import javax.inject.Inject

class ClearWatchProgressUseCase @Inject constructor(
    private val repository: WatchProgressRepository
) {
    suspend operator fun invoke() =
        repository.clearWatchProgress()
}