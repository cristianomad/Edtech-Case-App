package com.cristianomadeira.ulessontest.domain.di

import com.cristianomadeira.ulessontest.data.repository.BookmarkRepository
import com.cristianomadeira.ulessontest.data.repository.ChapterRepository
import com.cristianomadeira.ulessontest.data.repository.BookmarkRepositoryImpl
import com.cristianomadeira.ulessontest.data.repository.OfflineFirstChapterRepository
import com.cristianomadeira.ulessontest.data.repository.MilestoneRepositoryImpl
import com.cristianomadeira.ulessontest.data.repository.WatchProgressRepositoryImpl
import com.cristianomadeira.ulessontest.data.repository.MilestoneRepository
import com.cristianomadeira.ulessontest.data.repository.WatchProgressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindsChapterRepository(
        chapterRepository: OfflineFirstChapterRepository,
    ): ChapterRepository

    @Binds
    fun bindsBookmarkRepository(
        bookmarkRepository: BookmarkRepositoryImpl,
    ): BookmarkRepository

    @Binds
    fun bindsMilestoneRepository(
        milestoneRepository: MilestoneRepositoryImpl,
    ): MilestoneRepository

    @Binds
    fun bindsWatchProgressRepository(
        watchProgressRepository: WatchProgressRepositoryImpl,
    ): WatchProgressRepository
}