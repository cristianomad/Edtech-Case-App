package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.WatchProgressRepository
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWatchProgressUseCase @Inject constructor(
    private val repository: WatchProgressRepository
) {
    suspend operator fun invoke(): Flow<WatchProgress?> =
        repository.getWatchProgress()
}