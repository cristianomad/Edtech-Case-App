package com.cristianomadeira.ulessontest.ui.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.materialcolors.MaterialColors
import com.composables.materialcolors.get
import com.cristianomadeira.ulessontest.domain.model.Bookmark
import com.cristianomadeira.ulessontest.ui.theme.ULessonTestTheme

@Composable
fun BookmarkList(
    modifier: Modifier = Modifier,
    bookmarks: List<Bookmark>,
    onClick: (position: Long) -> Unit,
    onDelete: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            items(bookmarks, key = { bookmark -> bookmark.id }) { bookmark ->
                BookmarkListItem(
                    bookmark = bookmark,
                    onClick = onClick,
                    onDelete = onDelete
                )
            }
        }
    )
}

@Composable
fun BookmarkListItem(
    modifier: Modifier = Modifier,
    bookmark: Bookmark,
    onClick: (position: Long) -> Unit,
    onDelete: (id: Int) -> Unit
) {
    ListItem(
        modifier = modifier
            .padding(bottom = 6.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick(bookmark.position)
            },
        colors = ListItemDefaults.colors(
            containerColor = MaterialColors.Gray[300]
        ),
        headlineContent = {
            Text(
                text = bookmark.content,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingContent = {
            Text(
                text = DateUtils.formatElapsedTime(bookmark.position / 1000)
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier.clickable {
                    onDelete(bookmark.id)
                }
            )
        }
    )
}

@Preview
@Composable
fun BookmarkListItemPreview() {
    ULessonTestTheme {
        BookmarkListItem(
            bookmark = Bookmark(
                id = 1,
                lessonId = 1,
                content = "Some notes about this subject",
                position = 1000,
            ),
            onDelete = { _ -> },
            onClick = {}
        )
    }
}