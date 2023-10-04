package com.cristianomadeira.ulessontest.data.dataSource.remote

import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.ChapterDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : ChapterDataSource {

    override suspend fun fetchChapters(): Result<List<ChapterDto>> = runCatching {
        httpClient.get("/chapters").body()
    }
}