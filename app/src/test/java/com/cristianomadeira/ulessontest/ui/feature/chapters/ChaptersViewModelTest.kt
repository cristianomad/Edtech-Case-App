package com.cristianomadeira.ulessontest.ui.feature.chapters

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.AlwaysOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import com.cristianomadeira.ulessontest.domain.useCase.GetChaptersUseCase
import com.cristianomadeira.ulessontest.domain.useCase.GetLessonUseCase
import com.cristianomadeira.ulessontest.domain.useCase.GetWatchProgressUseCase
import com.cristianomadeira.ulessontest.domain.useCase.IsMilestoneAchievedUseCase
import com.cristianomadeira.ulessontest.domain.useCase.SetMilestoneAsSeenUseCase
import com.cristianomadeira.ulessontest.ui.state.ListUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChaptersViewModelTest {

    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(AlwaysOptionalStrategy)
    }

    @MockK
    lateinit var getChapterUseCase: GetChaptersUseCase
    @MockK
    lateinit var getLessonUseCase: GetLessonUseCase
    @MockK
    lateinit var getWatchProgressUseCase: GetWatchProgressUseCase
    @MockK
    lateinit var isMilestoneAchievedUseCase: IsMilestoneAchievedUseCase
    @MockK
    lateinit var setMilestoneAsSeenUseCase: SetMilestoneAsSeenUseCase

    private lateinit var viewModel: ChaptersViewModel

    @Before
    fun prepare() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this)

        coEvery { getChapterUseCase() } returns flowOf(Result.success(fixture()))
        coEvery { getWatchProgressUseCase() } returns flowOf(fixture<WatchProgress>())
        coEvery { getLessonUseCase(any()) } returns fixture()
        coEvery { isMilestoneAchievedUseCase() } returns flowOf(fixture())
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun initViewModel() {
        viewModel = ChaptersViewModel(
            getChapterUseCase,
            getLessonUseCase,
            getWatchProgressUseCase,
            isMilestoneAchievedUseCase,
            setMilestoneAsSeenUseCase
        )
    }

    @Test
    fun `test get chapters successfully`() {
        coEvery { getChapterUseCase() } returns flowOf(Result.success(fixture()))

        initViewModel()

        coVerify { getChapterUseCase() }

        assertThat(viewModel.chaptersState.value).isInstanceOf(ListUiState.Success::class.java)
    }

    @Test
    fun `test get chapters unsuccessfully`() {
        coEvery { getChapterUseCase() } returns flowOf(Result.failure(fixture()))

        initViewModel()

        coVerify { getChapterUseCase() }

        assertThat(viewModel.chaptersState.value).isInstanceOf(ListUiState.Empty::class.java)
    }

    @Test
    fun `test get watch progress`() {
        val lesson = fixture<Lesson>()
        val watchProgress = fixture<WatchProgress>()
        coEvery { getWatchProgressUseCase() } returns flowOf(watchProgress)
        coEvery { getLessonUseCase(any()) } returns lesson
        coEvery { getChapterUseCase() } returns flowOf(Result.success(fixture()))

        initViewModel()

        coVerify { getWatchProgressUseCase() }
        coVerify { getLessonUseCase(watchProgress.lessonId) }

        assertThat(viewModel.watchProgressState.value?.lessonId).isEqualTo(watchProgress.lessonId)
        assertThat(viewModel.watchProgressState.value?.progress).isEqualTo(watchProgress.progress)
        assertThat(viewModel.watchProgressState.value?.lesson?.id).isEqualTo(lesson.id)
        assertThat(viewModel.watchProgressState.value?.lesson?.title).isEqualTo(lesson.title)
        assertThat(viewModel.watchProgressState.value?.lesson?.thumbUrl).isEqualTo(lesson.thumbUrl)
        assertThat(viewModel.watchProgressState.value?.lesson?.videoUrl).isEqualTo(lesson.videoUrl)
    }

    @Test
    fun `test get watch progress is null`() {
        coEvery { getWatchProgressUseCase() } returns flowOf(null)

        initViewModel()

        coVerify { getWatchProgressUseCase() }

        assertThat(viewModel.watchProgressState.value).isNull()
    }

    @Test
    fun `test set milestone as seen `() {
        coEvery { setMilestoneAsSeenUseCase() } returns Unit

        initViewModel()

        viewModel.setMilestoneAsSeen()

        coVerify { setMilestoneAsSeenUseCase() }
    }

    @Test
    fun `test get adjacent of the first lesson on the list`() {
        val lessons = listOf(
            Lesson(
                id = 1,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            ),
            Lesson(
                id = 2,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            ),
            Lesson(
                id = 3,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            )
        )
        val fixture = listOf(
            Chapter(
                id = fixture<Int>(),
                title = fixture<String>(),
                enumeration = fixture<Int>(),
                lessons = lessons
            )
        )

        coEvery { getChapterUseCase() } returns flowOf(Result.success(fixture))

        initViewModel()

        val adjacentLessons = viewModel.getAdjacentLessons(lessonId = 1)

        assertThat(adjacentLessons.first().id).isEqualTo(lessons[1].id)
    }

    @Test
    fun `test get adjacent of the second lesson on the list`() {
        val lessons = listOf(
            Lesson(
                id = 1,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            ),
            Lesson(
                id = 2,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            ),
            Lesson(
                id = 3,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            )
        )
        val fixture = listOf(
            Chapter(
                id = fixture<Int>(),
                title = fixture<String>(),
                enumeration = fixture<Int>(),
                lessons = lessons
            )
        )

        coEvery { getChapterUseCase() } returns flowOf(Result.success(fixture))

        initViewModel()

        val adjacentLessons = viewModel.getAdjacentLessons(lessonId = 2)

        assertThat(adjacentLessons[0].id).isEqualTo(lessons[0].id)
        assertThat(adjacentLessons[1].id).isEqualTo(lessons[2].id)
    }

    @Test
    fun `test get adjacent of the last lesson on the list`() {
        val lessons = listOf(
            Lesson(
                id = 1,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            ),
            Lesson(
                id = 2,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            ),
            Lesson(
                id = 3,
                title = fixture(),
                thumbUrl = fixture(),
                videoUrl = fixture()
            )
        )
        val fixture = listOf(
            Chapter(
                id = fixture<Int>(),
                title = fixture<String>(),
                enumeration = fixture<Int>(),
                lessons = lessons
            )
        )

        coEvery { getChapterUseCase() } returns flowOf(Result.success(fixture))

        initViewModel()

        val adjacentLessons = viewModel.getAdjacentLessons(lessonId = 3)

        assertThat(adjacentLessons[0].id).isEqualTo(lessons[1].id)
    }

    @Test
    fun `test get adjacent when there isn't any lessons`() {
        coEvery { getChapterUseCase() } returns flowOf(Result.failure(fixture()))

        initViewModel()

        val adjacentLessons = viewModel.getAdjacentLessons(lessonId = 1)

        assertThat(adjacentLessons).isEmpty()
    }
}