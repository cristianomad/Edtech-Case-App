package com.cristianomadeira.ulessontest.ui.feature.lesson

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.cristianomadeira.ulessontest.R
import com.cristianomadeira.ulessontest.ui.components.BookmarkList
import com.cristianomadeira.ulessontest.ui.components.Loading
import com.cristianomadeira.ulessontest.ui.state.ScreenUiState
import com.cristianomadeira.ulessontest.ui.state.onSuccess
import com.cristianomadeira.ulessontest.ui.util.displaySystemUi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.serialization.Serializable

@Serializable
data class StringListNavArg(
    val list: List<String>
)

data class LessonScreenNavArgs(
    val lessonId: Int,
    val adjacentVideos: StringListNavArg
)

@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Destination(
    navArgsDelegate = LessonScreenNavArgs::class
)

@Composable
fun LessonScreen(
    navigator: DestinationsNavigator,
    viewModel: LessonViewModel = hiltViewModel()
) {
    val lessonState by viewModel.lessonState.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarkState.collectAsStateWithLifecycle()
    var bookmarkDialogVisibility by rememberSaveable { mutableStateOf(false) }
    var bookmarkAlertDialogText by rememberSaveable { mutableStateOf("") }
    val player = viewModel.player

    val onSaveBookmark = viewModel::addBookmark
    val onBookmarkDelete = viewModel::deleteBookmark
    val onBookmarkClick: (position: Long) -> Unit = { player.seekTo(it) }
    val openBookmarkDialog = {
        player.pause()
        bookmarkDialogVisibility = true
    }
    val dismissBookmarkDialog = {
        bookmarkDialogVisibility = false
        player.play()
    }

    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    LocalContext.current.displaySystemUi(isPortrait)

    BackHandler(enabled = true) {
        viewModel.onBackPressed()
        navigator.popBackStack()
    }

    Scaffold(
        topBar = {
            if (!isPortrait) return@Scaffold

            TopAppBar(
                title = {
                    lessonState.onSuccess {
                        Row {
                            Text(
                                text = data.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable {
                                openBookmarkDialog()
                            },
                        painter = painterResource(R.drawable.bookmark_add),
                        contentDescription = stringResource(R.string.add_bookmark_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(12.dp),
                        onClick = {
                            viewModel.onBackPressed()
                            navigator.popBackStack()
                        }
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
            )

        },
    ) { padding ->
        lessonState.apply {
            when (this) {
                is ScreenUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .background(if (isPortrait) Color.White else Color.Black)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            val modifier = if (isPortrait) {
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                            } else {
                                Modifier
                                    .fillMaxSize()
                            }

                            AndroidView(
                                factory = { context ->
                                    PlayerView(context).apply {
                                        this.player = player
                                        setShowNextButton(false)
                                        setShowPreviousButton(false)
                                        setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                                    }
                                },
                                modifier = modifier
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                        )
                        Row {
                            BookmarkList(
                                bookmarks = bookmarks,
                                onClick = onBookmarkClick,
                                onDelete = onBookmarkDelete
                            )
                        }
                    }
                }

                ScreenUiState.Loading -> Loading()
            }
        }
    }

    if (bookmarkDialogVisibility) {
        AlertDialog(
            modifier = Modifier
                .background(Color.White, MaterialTheme.shapes.medium),
            onDismissRequest = dismissBookmarkDialog,
            content = {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_bookmark_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        TextField(
                            value = bookmarkAlertDialogText,
                            onValueChange = { bookmarkAlertDialogText = it }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        TextButton(
                            onClick = dismissBookmarkDialog

                        ) {
                            Text(text = stringResource(id = android.R.string.cancel))
                        }
                        Button(
                            enabled = bookmarkAlertDialogText.trim().isNotEmpty(),
                            onClick = {
                                lessonState.onSuccess {
                                    onSaveBookmark(data.id, bookmarkAlertDialogText.trim())
                                }
                                dismissBookmarkDialog()
                            }
                        ) {
                            Text(stringResource(id = android.R.string.ok))
                        }
                    }
                }
            }
        )
    }
}