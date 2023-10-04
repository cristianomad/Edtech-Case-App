package com.cristianomadeira.ulessontest.ui.feature.chapters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.composables.materialcolors.MaterialColors
import com.composables.materialcolors.get
import com.cristianomadeira.ulessontest.R
import com.cristianomadeira.ulessontest.ui.components.ChapterList
import com.cristianomadeira.ulessontest.ui.components.Loading
import com.cristianomadeira.ulessontest.ui.components.ResumeProgressCard
import com.cristianomadeira.ulessontest.ui.feature.destinations.LessonScreenDestination
import com.cristianomadeira.ulessontest.ui.feature.lesson.StringListNavArg
import com.cristianomadeira.ulessontest.ui.state.ListUiState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Destination
@Composable
fun ChaptersScreen(
    navigator: DestinationsNavigator,
    viewModel: ChaptersViewModel = hiltViewModel()
) {
    val chaptersState by viewModel.chaptersState.collectAsStateWithLifecycle()
    val watchProgress by viewModel.watchProgressState.collectAsStateWithLifecycle()
    val isMilestoneAchieved by viewModel.isMilestoneAchievedState.collectAsStateWithLifecycle()

    val onLessonClick: (id: Int) -> Unit = { id ->
        navigator.navigate(
            LessonScreenDestination(
                lessonId = id,
                adjacentVideos = StringListNavArg(
                    viewModel.getAdjacentLessons(id).map { it.videoUrl }
                ),
            )
        )
    }

    val onMilestoneDismissed = {
        viewModel.setMilestoneAsSeen()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialColors.DeepOrange[300]
    ) {
        chaptersState.apply {
            when (this) {
                is ListUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        watchProgress?.let {
                            ResumeProgressCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                watchProgress = it,
                                onClick = onLessonClick
                            )
                        }
                        ChapterList(
                            chapters = data,
                            onLessonClick = onLessonClick,
                        )
                    }
                }

                ListUiState.Loading -> Loading()

                ListUiState.Empty -> {}
            }
        }
    }

    if (isMilestoneAchieved) {
        AlertDialog(
            modifier = Modifier
                .background(Color.White, MaterialTheme.shapes.medium),
            onDismissRequest = onMilestoneDismissed,
            content = {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.milestone_message),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Button(
                            onClick = onMilestoneDismissed
                        ) {
                            Text(stringResource(id = android.R.string.ok))
                        }
                    }
                }
            }
        )
    }
}