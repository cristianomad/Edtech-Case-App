package com.cristianomadeira.ulessontest.data.di

import com.cristianomadeira.ulessontest.data.dataSource.local.BookmarkDataSource
import com.cristianomadeira.ulessontest.data.dataSource.local.BookmarkDataSourceImpl
import com.cristianomadeira.ulessontest.data.dataSource.local.MilestoneDataSourceImpl
import com.cristianomadeira.ulessontest.data.dataSource.local.WatchProgressDataSourceImpl
import com.cristianomadeira.ulessontest.data.dataSource.local.MilestoneDataSource
import com.cristianomadeira.ulessontest.data.dataSource.local.WatchProgressDataSource
import com.cristianomadeira.ulessontest.data.dataSource.local.ChapterDataSource as LocalChapterDataSource
import com.cristianomadeira.ulessontest.data.dataSource.remote.ChapterDataSource as RemoteChapterDataSource
import com.cristianomadeira.ulessontest.data.dataSource.local.ChapterDataSourceImpl as DefaultLocalChapterDataSource
import com.cristianomadeira.ulessontest.data.dataSource.remote.ChapterDataSourceImpl as DefaultRemoteChapterDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindRemoteChapterDataSource(
        remoteChapterDataSource: DefaultRemoteChapterDataSource
    ): RemoteChapterDataSource

    @Binds
    fun bindLocalChapterDataSource(
        localChapterDataSource: DefaultLocalChapterDataSource
    ): LocalChapterDataSource

    @Binds
    fun bindBookmarkDataSource(
        bookmarkDataSource: BookmarkDataSourceImpl
    ): BookmarkDataSource

    @Binds
    fun bindMilestoneDataSource(
        milestoneDataSource: MilestoneDataSourceImpl
    ): MilestoneDataSource

    @Binds
    fun bindWatchProgressDataSource(
        watchProgressDataSource: WatchProgressDataSourceImpl
    ): WatchProgressDataSource
}