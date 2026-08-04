package com.song.demos.presentation.demos.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeBeige
import com.song.core.presentation.designsystem.components.SongScribeButton
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.designsystem.extension.addDefaultStartPadding
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.ui.util.formatAsDuration
import com.song.demos.presentation.demos.model.DemoModel
import com.song.demos.presentation.demos.model.RecordingItem

@Composable
fun DemoListItem(
    modifier: Modifier = Modifier,
    demoModel: DemoModel
) {
    val primaryRecording = remember(demoModel.recordings) {
        demoModel.recordings.first { it.isPrimary }
    }
    val playbackProgress = if (primaryRecording.duration > 0) {
        primaryRecording.currentDuration.toFloat() / primaryRecording.duration.toFloat()
    } else {
        0f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = playbackProgress.coerceIn(0f, 1f),
        label = "playbackProgress"
    )
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
            .background(color = demoModel.colorLabel)
            .height(200.dp)
            .width(6.dp)
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
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = demoModel.colorLabel)
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
                        text = demoModel.title,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = demoModel.date,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        SongScribeBeige(
                            text = "+${demoModel.moreRecordingCount} more",
                            modifier = Modifier
                                .addDefaultStartPadding(),
                            containerColor = MaterialTheme.colorScheme.primary,
                            textColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
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
                text = primaryRecording.title,
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
                        .addDefaultStartPadding(),
                    text = primaryRecording.currentDuration.formatAsDuration(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .addDefaultStartPadding()
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = MaterialTheme.colorScheme.primary)
                    )
                }
                Text(
                    modifier = Modifier
                        .addDefaultStartPadding(),
                    text = primaryRecording.duration.formatAsDuration(),
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
               val list = demoModel.genres
               list.forEach {
                   SongScribeBeige(
                       text = it,
                       modifier = Modifier.addDefaultTopPadding(),
                       containerColor = MaterialTheme.colorScheme.outlineVariant,
                       textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                   )
               }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DemoListItemPreview() {
    SongScribeTheme {
        DemoListItem(
            demoModel = DemoModel(
                id = "1",
                title = "Yellow Stone",
                date = "21/07/2022",
                colorLabel = MaterialTheme.colorScheme.tertiaryFixed,
                recordings = listOf(
                    RecordingItem(
                        id = "1",
                        title = "First Take",
                        duration = 33,
                        isPrimary = true,
                        recording = "",
                        currentDuration = 15,
                    ),
                    RecordingItem(
                        id = "2",
                        title = "Second Take",
                        duration = 40,
                        isPrimary = false,
                        recording = "",
                        currentDuration = 15,
                    )
                ),
                genres = listOf("piano", "guitar", "drums", "piano", "guitar", "drums", "piano", "guitar", "drums"),
                moreRecordingCount = 2
            )
        )
    }
}