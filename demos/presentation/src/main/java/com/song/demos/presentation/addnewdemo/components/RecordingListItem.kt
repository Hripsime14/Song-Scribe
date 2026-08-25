package com.song.demos.presentation.addnewdemo.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeButton
import com.song.core.presentation.designsystem.extension.addDefaultStartPadding
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.LightStarColor
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.formatAsDuration
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi

@Composable
fun RecordingListItem(
    modifier: Modifier = Modifier,
    recording: RecordingItemUi,
    onPlayPauseClick: () -> Unit = {},
    onSetPrimaryClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val playbackProgress = if (recording.durationSeconds > 0) {
        (recording.currentDuration.toFloat() / recording.durationSeconds.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
    val animatedProgress = remember { Animatable(playbackProgress) }

    LaunchedEffect(recording.isPlaying, recording.id) {
        if (recording.isPlaying && recording.durationSeconds > 0) {
            val remainingSeconds = recording.durationSeconds - recording.currentDuration
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
            .addDefaultTopPadding()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            onClick = onSetPrimaryClick,
                            interactionSource = null,
                            indication = null
                        ),
                    imageVector = if (recording.isPrimary) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (recording.isPrimary) {
                        stringResource(R.string.primary_recording)
                    } else {
                        stringResource(R.string.set_as_primary_recording)
                    },
                    tint = if (recording.isPrimary) LightStarColor else MaterialTheme.colorScheme.secondary
                )
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    state = recording.titleState,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                        .clickable(
                            onClick = onDeleteClick,
                            interactionSource = null,
                            indication = null
                        ),
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_recording),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp, start = 8.dp)) {
                SongScribeButton(
                    icon = if (recording.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    onClick = onPlayPauseClick
                )
                Text(
                    modifier = Modifier.addDefaultStartPadding(),
                    text = recording.currentDuration.formatAsDuration(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .addDefaultStartPadding()
                        .height(6.dp)
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
                    modifier = Modifier.addDefaultStartPadding(),
                    text = recording.durationSeconds.formatAsDuration(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordingListItemPreview() {
    SongScribeTheme {
        Column {
            RecordingListItem(
                recording = RecordingItemUi(
                    id = "1",
                    titleState = TextFieldState("First take"),
                    filePath = "",
                    durationSeconds = 42,
                    currentDuration = 15,
                    isPrimary = true
                )
            )
            RecordingListItem(
                recording = RecordingItemUi(
                    id = "2",
                    titleState = TextFieldState("Take 2"),
                    filePath = "",
                    durationSeconds = 20,
                    isPrimary = false
                )
            )
        }
    }
}
