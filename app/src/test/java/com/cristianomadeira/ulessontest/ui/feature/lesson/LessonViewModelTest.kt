package com.cristianomadeira.ulessontest.ui.feature.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.data.service.MediaDownloadManager
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import com.cristianomadeira.ulessontest.domain.model.Lesson
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import com.cristianomadeira.ulessontest.domain.useCase.AddBookmarkUseCase
import com.cristianomadeira.ulessontest.domain.useCase.ClearWatchProgressUseCase
import com.cristianomadeira.ulessontest.domain.useCase.DeleteBookmarkUseCase
import com.cristianomadeira.ulessontest.domain.useCase.GetBookmarksUseCase
import com.cristianomadeira.ulessontest.domain.useCase.GetLessonUseCase
import com.cristianomadeira.ulessontest.domain.useCase.GetWatchProgressUseCase
import com.cristianomadeira.ulessontest.domain.useCase.SetNewMilestoneUseCase
import com.cristianomadeira.ulessontest.domain.useCase.SetWatchProgressUseCase
import com.cristianomadeira.ulessontest.ui.util.ConnectivityUtil
import com.google.common.truth.Truth.assertThat
import io.ktor.utils.io.core.toByteArray
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LessonViewModelTest {
    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    @MockK
    lateinit var getLessonUseCase: GetLessonUseCase
    @MockK
    lateinit var getBookmarksUseCase: GetBookmarksUseCase
    @MockK
    lateinit var addBookmarkUseCase: AddBookmarkUseCase
    @MockK
    lateinit var deleteBookmarkUseCase: DeleteBookmarkUseCase
    @MockK
    lateinit var getWatchProgressUseCase: GetWatchProgressUseCase
    @MockK
    lateinit var setWatchProgressUseCase: SetWatchProgressUseCase
    @MockK
    lateinit var clearWatchProgressUseCase: ClearWatchProgressUseCase
    @MockK
    lateinit var setNewMilestoneUseCase: SetNewMilestoneUseCase
    @MockK
    lateinit var connectivityUtil: ConnectivityUtil
    @MockK
    lateinit var mediaDownloadManager: MediaDownloadManager
    @MockK
    lateinit var player: ExoPlayer
    @MockK
    lateinit var mediaItem: MediaItem

    private lateinit var viewModel: LessonViewModel
    private val lessonId = 1
    private val adjacentVideos = listOf("video-1" ,"video-2","video-3")

    @Before
    fun prepare() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        mockkStatic(MediaItem::class)
        every { MediaItem.fromUri(any<String>()) } returns mediaItem
        every { connectivityUtil.isWiFiConnected() } returns true
        every { mediaDownloadManager.addDownload(any()) } returns Unit
        coEvery { getLessonUseCase(any()) } returns fixture<Lesson>()
        coEvery { getBookmarksUseCase(any()) } returns flowOf(fixture<List<Bookmark>>())
        coEvery { getWatchProgressUseCase() } returns flowOf(fixture<WatchProgress?>())
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun initViewModel() {
        val adjacentVideosArg = StringListNavArg(adjacentVideos)
        val savedStateHandle = SavedStateHandle().apply {
            set("lessonId", lessonId)
            set("adjacentVideos", Json.encodeToString(adjacentVideosArg).toByteArray())
        }

        viewModel = LessonViewModel(
            savedStateHandle,
            getLessonUseCase,
            getBookmarksUseCase,
            addBookmarkUseCase,
            deleteBookmarkUseCase,
            getWatchProgressUseCase,
            setWatchProgressUseCase,
            clearWatchProgressUseCase,
            setNewMilestoneUseCase,
            connectivityUtil,
            mediaDownloadManager,
            player
        )
    }

    @Test
    fun `test init player when there is no saved progress`() {
        every { mediaDownloadManager.addDownload(any()) } returns Unit
        every { player.prepare() } returns Unit
        every { player.setMediaItem(any()) } returns Unit
        every { player.playWhenReady } returns true
        coEvery { getWatchProgressUseCase() } returns flowOf(null)

        initViewModel()

        verify { player.prepare() }
        verify { player.setMediaItem(any()) }
        coVerify { getWatchProgressUseCase() }
        assertThat(player.playWhenReady).isTrue()
    }

    @Test
    fun `test init player when there is saved progress`() {
        val fixture = WatchProgress(
            lessonId = lessonId,
            progress = fixture()
        )
        every { mediaDownloadManager.addDownload(any()) } returns Unit
        every { player.prepare() } returns Unit
        every { player.setMediaItem(any()) } returns Unit
        every { player.playWhenReady } returns true
        every { player.seekTo(any()) } returns Unit
        coEvery { getWatchProgressUseCase() } returns flowOf(fixture)
        coEvery { getLessonUseCase(any()) } returns fixture<Lesson>().copy(id = lessonId)

        initViewModel()

        verify { player.prepare() }
        verify { player.setMediaItem(any()) }
        verify { player.seekTo(fixture.progress) }
        coVerify { getWatchProgressUseCase() }
        assertThat(player.playWhenReady).isTrue()
    }

    @Test
    fun `test get bookmarks`() {
        val fixture = fixture<List<Bookmark>>()
        coEvery { getBookmarksUseCase(any()) } returns flowOf(fixture)

        initViewModel()

        coVerify { getBookmarksUseCase(any()) }

        viewModel.bookmarkState.value.forEachIndexed { index, bookmark ->
            assertThat(bookmark).isEqualTo(fixture[index])
        }
    }

    @Test
    fun `test add bookmark`() {
        val fixture = Bookmark(
            lessonId = fixture(),
            content = fixture(),
            position = fixture()
        )

        coEvery { addBookmarkUseCase(any()) } returns Unit
        every { player.currentPosition } returns fixture.position

        initViewModel()

        viewModel.addBookmark(
            lessonId = fixture.lessonId,
            content = fixture.content,
        )

        coVerify { addBookmarkUseCase(fixture) }
    }

    @Test
    fun `test delete bookmark`() {
        val fixture = fixture<Int>()

        coEvery { deleteBookmarkUseCase(any()) } returns Unit

        initViewModel()

        viewModel.deleteBookmark(fixture)

        coVerify { deleteBookmarkUseCase(fixture) }
    }

    @Test
    fun `test get lesson`() {
        coEvery { getLessonUseCase(any()) } returns fixture<Lesson>()

        initViewModel()

        coVerify { getLessonUseCase(any()) }
    }

    @Test
    fun `test save watch progress`() {
        every { player.currentPosition } returns 1000L
        every { player.playbackState } returns STATE_READY
        coEvery { setWatchProgressUseCase(any()) } returns Unit

        initViewModel()

        viewModel.onBackPressed()

        coVerify { setWatchProgressUseCase(any()) }
    }

    @Test
    fun `test download adjacent videos when WiFi is on`() {
        every { connectivityUtil.isWiFiConnected() } returns true
        every { mediaDownloadManager.addDownload(any()) } returns Unit

        initViewModel()

        // 3 adjacent videos + 1 current video
        verify(exactly = 4) { mediaDownloadManager.addDownload(any()) }
    }

    @Test
    fun `test download adjacent videos when WiFi is off`() {
        every { connectivityUtil.isWiFiConnected() } returns false
        every { mediaDownloadManager.addDownload(any()) } returns Unit

        initViewModel()

        // Only current video
        verify(exactly = 1) { mediaDownloadManager.addDownload(any()) }
    }

    @Test
    fun `test clear watch progress`() {
        coEvery { clearWatchProgressUseCase() } returns Unit

        initViewModel()
        viewModel.clearResumeWatchProgress()

        // Only current video
        coVerify { clearWatchProgressUseCase() }
    }

    @Test
    fun `test set new milestone`() {
        coEvery { setNewMilestoneUseCase() } returns Unit

        initViewModel()
        viewModel.setNewMilestone()

        // Only current video
        coVerify { setNewMilestoneUseCase() }
    }
}