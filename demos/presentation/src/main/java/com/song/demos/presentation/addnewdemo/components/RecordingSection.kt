package com.song.demos.presentation.addnewdemo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi

@Composable
fun RecordingSection(
    modifier: Modifier = Modifier,
    isRecording: Boolean = false,
    recordingSeconds: Int = 0,
    recordings: List<RecordingItemUi> = emptyList(),
    onToggleRecording: () -> Unit = {},
    onPlayPauseRecording: (String) -> Unit = {},
    onSetPrimaryRecording: (String) -> Unit = {},
    onDeleteRecording: (String) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .addDefaultTopPadding()
                .clip(RoundedCornerShape(32.dp))
                .height(220.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            RecordingView(
                isRecording = isRecording,
                recordingSeconds = recordingSeconds,
                onToggleRecording = onToggleRecording,
            )
        }
        recordings.forEach { recording ->
            key(recording.id) {
                RecordingListItem(
                    recording = recording,
                    onPlayPauseClick = { onPlayPauseRecording(recording.id) },
                    onSetPrimaryClick = { onSetPrimaryRecording(recording.id) },
                    onDeleteClick = { onDeleteRecording(recording.id) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SectionWithBackgroundPreview() {
    SongScribeTheme {
        RecordingSection(
            modifier = Modifier,
            recordings = listOf(
                RecordingItemUi(
                    id = "1",
                    titleState = TextFieldState("First take"),
                    filePath = "",
                    durationSeconds = 42,
                    isPrimary = true
                ),
                RecordingItemUi(
                    id = "2",
                    titleState = TextFieldState("Take 2"),
                    filePath = "",
                    durationSeconds = 20,
                    isPrimary = false
                )
            )
        )
    }
}
