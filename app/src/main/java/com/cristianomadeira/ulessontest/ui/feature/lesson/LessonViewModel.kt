package com.cristianomadeira.ulessontest.ui.feature.lesson

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PRIVATE
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
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
import com.cristianomadeira.ulessontest.ui.feature.destinations.LessonScreenDestination
import com.cristianomadeira.ulessontest.ui.state.ScreenUiState
import com.cristianomadeira.ulessontest.ui.util.ConnectivityUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class LessonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getLessonUseCase: GetLessonUseCase,
    private val getBookmarkUseCase: GetBookmarksUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val getWatchProgressUseCase: GetWatchProgressUseCase,
    private val setWatchProgressUseCase: SetWatchProgressUseCase,
    private val clearWatchProgressUseCase: ClearWatchProgressUseCase,
    private val setNewMilestoneUseCase: SetNewMilestoneUseCase,
    private val connectivityUtil: ConnectivityUtil,
    private val mediaDownloadManager: MediaDownloadManager,
    val player: ExoPlayer
) : ViewModel() {
    private val navArgs = LessonScreenDestination.argsFrom(savedStateHandle)

    private var _lessonState = MutableStateFlow<ScreenUiState<Lesson>>(ScreenUiState.Loading)
    val lessonState = _lessonState.asStateFlow()

    private var _bookmarkState = MutableStateFlow<List<Bookmark>>(listOf())
    val bookmarkState = _bookmarkState.asStateFlow()

    val onBackPressed: () -> Unit = {
        if (player.currentPosition > 0 && player.playbackState != STATE_ENDED) {
            saveWatchProgress()
        }
    }

    init {
        val lessonId = navArgs.lessonId
        val adjacentVideos = navArgs.adjacentVideos.list

        downloadVideosOnlyOnWiFi(adjacentVideos)
        getLesson(lessonId)
        getBookmarks(lessonId)
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }

    fun addBookmark(lessonId: Int, content: String) {
        viewModelScope.launch {
            addBookmarkUseCase(
                Bookmark(
                    lessonId = lessonId,
                    content = content,
                    position = player.currentPosition
                )
            )
        }
    }

    fun deleteBookmark(id: Int) {
        viewModelScope.launch {
            deleteBookmarkUseCase(id)
        }
    }

    private fun getLesson(id: Int) {
        viewModelScope.launch {
            val lesson = getLessonUseCase(id)

            initPlayer(lesson.id, lesson.videoUrl)
            _lessonState.value = ScreenUiState.Success(lesson)
        }
    }

    private fun initPlayer(lessonId: Int, videoUrl: String) {
        viewModelScope.launch {
            mediaDownloadManager.addDownload(videoUrl)

            player.apply {
                prepare()
                setMediaItem(MediaItem.fromUri(videoUrl))

                getWatchProgressUseCase().firstOrNull()?.let { watchProgress ->
                    if (watchProgress.lessonId == lessonId) {
                        seekTo(watchProgress.progress)
                    }
                }

                playWhenReady = true

                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)

                        if (STATE_ENDED == playbackState) {
                            clearResumeWatchProgress()
                            setNewMilestone()
                        }
                    }
                })
            }
        }
    }

    private fun getBookmarks(lessonId: Int) {
        viewModelScope.launch {
            getBookmarkUseCase(lessonId).collect {
                _bookmarkState.value = it
            }
        }
    }

    private fun saveWatchProgress() {
        viewModelScope.launch {
            setWatchProgressUseCase(
                WatchProgress(
                    lessonId = navArgs.lessonId,
                    progress = player.currentPosition
                )
            )
        }
    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun clearResumeWatchProgress() {
        viewModelScope.launch {
            clearWatchProgressUseCase()
        }
    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun setNewMilestone() {
        viewModelScope.launch {
            setNewMilestoneUseCase()
        }
    }

    private fun downloadVideosOnlyOnWiFi(videoUrls: List<String>) {
        if (!connectivityUtil.isWiFiConnected()) return

        videoUrls.forEach { videoUrl ->
            mediaDownloadManager.addDownload(videoUrl)
        }
    }
}