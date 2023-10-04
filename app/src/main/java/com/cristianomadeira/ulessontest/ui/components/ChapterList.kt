package com.cristianomadeira.ulessontest.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.composables.materialcolors.MaterialColors
import com.composables.materialcolors.get
import com.cristianomadeira.ulessontest.R
import com.cristianomadeira.ulessontest.domain.model.Chapter
import com.cristianomadeira.ulessontest.domain.model.Lesson
import com.cristianomadeira.ulessontest.domain.model.WatchProgress
import com.cristianomadeira.ulessontest.ui.theme.ULessonTestTheme

@Composable
fun ChapterList(
    chapters: List<Chapter>,
    onLessonClick: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            items(chapters, key = { chapter -> chapter.id }) { chapter ->
                ChapterCard(
                    chapter = chapter,
                    onLessonClick = onLessonClick
                )
            }
        }
    )
}

@Composable
fun ChapterCard(
    modifier: Modifier = Modifier,
    chapter: Chapter,
    onLessonClick: (id: Int) -> Unit
) {
    var contentVisible by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                contentVisible = !contentVisible
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Row {
                Row(
                    modifier = modifier
                        .weight(1f)
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp
                            ),
                            text = chapter.title,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.padding(
                                top = 6.dp,
                                end = 16.dp,
                                bottom = 16.dp,
                                start = 16.dp
                            ),
                            text = pluralStringResource(
                                R.plurals.chapter_lessons,
                                chapter.lessons.size,
                                chapter.lessons.size
                            ),
                            color = MaterialColors.Gray[600],
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                Row(
                    modifier = modifier
                        .padding(end = 10.dp)
                        .weight(0.1f)
                        .align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = if (contentVisible)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = "",
                    )
                }
            }
            AnimatedVisibility(visible = contentVisible) {
                LessonList(
                    lessons = chapter.lessons,
                    onLessonClick = onLessonClick
                )
            }
        }
    }
}

@Composable
fun LessonList(
    modifier: Modifier = Modifier,
    lessons: List<Lesson>,
    onLessonClick: (id: Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        lessons.forEach { lesson ->
            LessonListItem(
                lesson = lesson,
                onLessonClick = onLessonClick
            )
        }
    }
}

@Composable
fun LessonListItem(
    modifier: Modifier = Modifier,
    lesson: Lesson,
    onLessonClick: (id: Int) -> Unit
) {
    ListItem(
        modifier = modifier
            .padding(bottom = 6.dp, start = 16.dp, end = 16.dp)
            .clickable {
                onLessonClick(lesson.id)
            },
        colors = ListItemDefaults.colors(
            containerColor = Color.White
        ),
        headlineContent = { Text(lesson.title) },
        leadingContent = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(lesson.thumbUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = lesson.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
            )
        },
    )
}

@Composable
fun ResumeProgressCard(
    modifier: Modifier = Modifier,
    watchProgress: WatchProgress,
    onClick: (id: Int) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "",
                )
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.continue_watching_title),
                    fontWeight = FontWeight.Bold
                )
            }

            watchProgress.lesson?.let { lesson ->
                LessonListItem(
                    lesson = lesson,
                    onLessonClick = {
                        onClick(watchProgress.lessonId)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ChapterListItemPreview() {
    ULessonTestTheme {
        ChapterCard(
            chapter = Chapter(
                id = 1,
                enumeration = 1,
                title = "Chapter 1",
                lessons = listOf(
                    Lesson(
                        id = 1,
                        title = "Lesson 1",
                        thumbUrl = "thumbUrl",
                        videoUrl = "url",
                    )
                )
            ),
            onLessonClick = { _ -> }
        )
    }
}