package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.WatchProgressRepository
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import javax.inject.Inject

class SetWatchProgressUseCase @Inject constructor(
    private val repository: WatchProgressRepository
) {
    suspend operator fun invoke(watchProgress: WatchProgress) =
        repository.setWatchProgress(watchProgress)
}