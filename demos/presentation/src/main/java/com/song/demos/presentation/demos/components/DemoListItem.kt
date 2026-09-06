package com.song.demos.presentation.demos.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeBeige
import com.song.core.presentation.designsystem.components.SongScribeButton
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.designsystem.extension.addDefaultStartPadding
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.ui.util.formatAsDuration
import com.song.demos.presentation.demos.model.DemoUi

@Composable
fun DemoListItem(
    modifier: Modifier = Modifier,
    demoUi: DemoUi,
    onPlayPauseClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onItemClick: () -> Unit = {}
) {
    val primaryRecording = demoUi.recording ?: return
    val playbackProgress = if (primaryRecording.duration > 0) {
        (primaryRecording.currentDuration.toFloat() / primaryRecording.duration.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    val animatedProgress = remember { Animatable(playbackProgress) }

    LaunchedEffect(primaryRecording.isPlaying, primaryRecording.id) {
        if (primaryRecording.isPlaying && primaryRecording.duration > 0) {
            val remainingSeconds = primaryRecording.duration - primaryRecording.currentDuration
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = (remainingSeconds * 1000).coerceAtLeast(1),
                    easing = LinearEasing
                )
            )
        } else {
            animatedProgress.snapTo(playbackProgress)
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(32.dp)
            )
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(enabled = true, onClick = onItemClick)
    ) {
        Box(modifier = Modifier
            .background(color = demoUi.colorLabel)
            .height(200.dp)
            .width(6.dp)
            .padding(8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                Icon(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = demoUi.colorLabel)
                        .padding(8.dp),
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Note",
                    tint = MaterialTheme.colorScheme.scrim
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
                        text = demoUi.title,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = demoUi.date,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        if (demoUi.moreRecordingCount > 0) {
                            SongScribeBeige(
                                text = "+${demoUi.moreRecordingCount} more",
                                modifier = Modifier
                                    .addDefaultStartPadding(),
                                containerColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = onDeleteClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
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
                    icon = if (primaryRecording.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    onClick = onPlayPauseClick
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
                            .fillMaxWidth(animatedProgress.value)
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
               val list = demoUi.genres
               list.forEach {
                   SongScribeBeige(
                       text = it,
                       modifier = Modifier.addDefaultTopPadding(),
                       containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
//        DemoListItem(
//            demoUi = DemoUi(
//                id = "1",
//                title = "Yellow Stone",
//                date = "21/07/2022",
//                colorLabel = MaterialTheme.colorScheme.tertiaryFixed,
//                recordings = listOf(
//                    RecordingUi(
//                        id = "1",
//                        title = "First Take",
//                        duration = 33,
//                        isPrimary = true,
//                        recording = "",
//                        currentDuration = 15,
//                    ),
//                    RecordingUi(
//                        id = "2",
//                        title = "Second Take",
//                        duration = 40,
//                        isPrimary = false,
//                        recording = "",
//                        currentDuration = 15,
//                    )
//                ),
//                genres = listOf("piano", "guitar", "drums", "piano", "guitar", "drums", "piano", "guitar", "drums"),
//                moreRecordingCount = 2
//            )
//        )
    }
}