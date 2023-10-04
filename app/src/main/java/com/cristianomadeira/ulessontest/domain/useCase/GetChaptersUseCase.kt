package com.cristianomadeira.ulessontest.domain.useCase

import com.cristianomadeira.ulessontest.data.repository.ChapterRepository
import com.cristianomadeira.ulessontest.domain.model.Chapter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChaptersUseCase @Inject constructor(
    private val repository: ChapterRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Chapter>>> =
        repository.getChapters()
}