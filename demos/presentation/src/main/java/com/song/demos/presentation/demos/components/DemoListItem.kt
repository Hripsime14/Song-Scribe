package com.song.demos.presentation.demos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.DemoGenreView
import com.song.core.presentation.designsystem.components.SongScribeButton
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.addDefaultStartPadding
import com.song.core.presentation.ui.util.addDefaultTopPadding
import com.song.demos.presentation.R

@Composable
fun DemoListItem(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        Box(modifier = modifier
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .height(200.dp)
            .width(8.dp)
            .padding(8.dp)
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                Icon(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(8.dp),
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Note",
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterVertically)
                        .addDefaultStartPadding()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "21/07/2022",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Note",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            PrimaryDemoView(
                modifier = Modifier
                    .fillMaxWidth()
                    .addDefaultTopPadding()
                    .addDefaultTopPadding(),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(R.string.first_take),
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .addDefaultTopPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SongScribeButton(
                    icon = Icons.Default.PlayArrow,
                    onClick = {}
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .addDefaultStartPadding(),
                    text = "0:33",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    modifier = Modifier
                        .addDefaultStartPadding(),
                    text = "3:01",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .addDefaultTopPadding()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
               val list = listOf("piano", "guitar", "drums", "piano", "guitar", "drums", "piano", "guitar", "drums")
               list.forEach {
                   DemoGenreView(
                       text = it,
                       modifier = Modifier.addDefaultTopPadding()
                   )
               }
            }
        }

    }
}

@Preview
@Composable
private fun DemoListItemPreview() {
    SongScribeTheme {
        DemoListItem()
    }
}