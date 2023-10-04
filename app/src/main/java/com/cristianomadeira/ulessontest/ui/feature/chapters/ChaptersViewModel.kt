package com.cristianomadeira.ulessontest.ui.feature.chapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import com.cristianomadeira.ulessontest.domain.useCase.GetChaptersUseCase
import com.cristianomadeira.ulessontest.domain.useCase.GetLessonUseCase
import com.cristianomadeira.ulessontest.domain.useCase.GetWatchProgressUseCase
import com.cristianomadeira.ulessontest.domain.useCase.IsMilestoneAchievedUseCase
import com.cristianomadeira.ulessontest.domain.useCase.SetMilestoneAsSeenUseCase
import com.cristianomadeira.ulessontest.ui.state.ListUiState
import com.cristianomadeira.ulessontest.ui.state.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChaptersViewModel @Inject constructor(
    private val getChaptersUseCase: GetChaptersUseCase,
    private val getLessonUseCase: GetLessonUseCase,
    private val getWatchProgressUseCase: GetWatchProgressUseCase,
    private val isMilestoneAchievedUseCase: IsMilestoneAchievedUseCase,
    private val setMilestoneAsSeenUseCase: SetMilestoneAsSeenUseCase
) : ViewModel() {

    private var _chaptersState = MutableStateFlow<ListUiState<List<Chapter>>>(ListUiState.Loading)
    val chaptersState = _chaptersState.asStateFlow()

    private var _watchProgressState = MutableStateFlow<WatchProgress?>(null)
    val watchProgressState = _watchProgressState.asStateFlow()

    private var _isMilestoneAchievedState = MutableStateFlow(false)
    val isMilestoneAchievedState = _isMilestoneAchievedState.asStateFlow()

    init {
        getChapters()
        getWatchProgress()
        getMilestone()
    }
    private fun getChapters() {
        viewModelScope.launch {
            getChaptersUseCase().collect { result ->
                result.fold(
                    onSuccess = { chapters ->
                        _chaptersState.value = ListUiState.Success(chapters)
                    },
                    onFailure = {
                        _chaptersState.value = ListUiState.Empty
                    }
                )
            }
        }
    }

    private fun getWatchProgress() {
        viewModelScope.launch {
            getWatchProgressUseCase().collect { result ->
                result?.let {
                    val lesson = getLessonUseCase(result.lessonId)
                    val watchProgress = it.copy(lesson = lesson)
                    _watchProgressState.value = watchProgress
                    return@collect
                }

                _watchProgressState.value = null
            }
        }
    }

    private fun getMilestone() {
        viewModelScope.launch {
            isMilestoneAchievedUseCase().collect { isMilestoneAchieved ->
                _isMilestoneAchievedState.value = isMilestoneAchieved
            }
        }
    }

    fun setMilestoneAsSeen() {
        viewModelScope.launch {
            setMilestoneAsSeenUseCase()
        }
    }

    fun getAdjacentLessons(lessonId: Int): List<Lesson> {
        _chaptersState.value.let { state ->
            state.onSuccess {
                val chapter = data.find { it.lessons.any { lesson -> lesson.id == lessonId } }
                val lesson = chapter?.lessons?.find { it.id == lessonId }
                val lessonIndex = chapter?.lessons?.indexOf(lesson)
                val previousLesson = chapter?.lessons?.getOrNull(lessonIndex?.minus(1) ?: 0)
                val nextLesson = chapter?.lessons?.getOrNull(lessonIndex?.plus(1) ?: 0)

                return listOfNotNull(previousLesson, nextLesson)
            }
        }

        return emptyList()
    }
}