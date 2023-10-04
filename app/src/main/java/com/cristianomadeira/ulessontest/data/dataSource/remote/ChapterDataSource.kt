package com.cristianomadeira.ulessontest.data.dataSource.remote

import com.cristianomadeira.ulessontest.data.dataSource.remote.dto.ChapterDto

interface ChapterDataSource {
    suspend fun fetchChapters(): Result<List<ChapterDto>>
}