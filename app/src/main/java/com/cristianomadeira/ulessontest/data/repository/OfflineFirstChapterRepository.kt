package com.cristianomadeira.ulessontest.data.repository

import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.toModel
import com.cristianomadeira.ulessontest.data.di.CoroutinesDispatchers.IO
import com.cristianomadeira.ulessontest.data.di.Dispatcher
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.cristianomadeira.ulessontest.data.dataSource.local.ChapterDataSource as LocalChapterDataSource
import com.cristianomadeira.ulessontest.data.dataSource.remote.ChapterDataSource as RemoteChapterDataSource

class OfflineFirstChapterRepository @Inject constructor(
    private val remoteDataSource: RemoteChapterDataSource,
    private val localDataSource: LocalChapterDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ChapterRepository {
    override suspend fun getChapters(): Flow<Result<List<Chapter>>> = withContext(ioDispatcher) {
        val localChapters = localDataSource.getChapters().map {
            if (it.isNotEmpty()) {
                Result.success(it)
            } else {
                Result.failure(Exception("No chapters found"))
            }
        }
        val remoteChapters = fetchChapters()

        localChapters.combine(remoteChapters) { local, remote ->
            local.fold(
                onSuccess = {
                    local
                },
                onFailure = {
                    remote
                }
            )
        }
    }

    private suspend fun fetchChapters(): Flow<Result<List<Chapter>>> = withContext(ioDispatcher) {
        remoteDataSource.fetchChapters().map { result ->
            result.map {
                it.toModel()
            }
        }.fold(
            onSuccess = { chapters ->
                localDataSource.removeChapters()
                localDataSource.insertChapters(chapters)
                flowOf(Result.success(chapters))
            },
            onFailure = { error ->
                flowOf(Result.failure(error))
            }
        )
    }

    override suspend fun getLesson(id: Int): Lesson = withContext(ioDispatcher) {
        localDataSource.getLesson(id)
    }
}