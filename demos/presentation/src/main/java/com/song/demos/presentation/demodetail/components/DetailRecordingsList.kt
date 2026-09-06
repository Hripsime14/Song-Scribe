package com.song.demos.presentation.demodetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribePositiveButton
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.components.RecordingListItem
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi

@Composable
fun DetailRecordingsList(
    modifier: Modifier = Modifier,
    recordings: List<RecordingItemUi>,
    isAddingRecording: Boolean = false,
    isRecording: Boolean = false,
    recordingSeconds: Int = 0,
    newRecordingLabelState: TextFieldState = TextFieldState(),
    onNewRecordingClick: () -> Unit = {},
    onCloseNewRecordingClick: () -> Unit = {},
    onToggleRecording: () -> Unit = {},
    onAddNewRecordingClick: () -> Unit = {},
    onPlayPauseRecording: (String) -> Unit = {},
    onSetPrimaryRecording: (String) -> Unit = {},
    onDeleteRecording: (String) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .addDefaultTopPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.recordings_section),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            SongScribePositiveButton (
                icon = Icons.Default.Add,
                text = stringResource(R.string.add),
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        onClick = onNewRecordingClick,
                        interactionSource = null,
                        indication = null
                    ),
                containerColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onNewRecordingClick,
                cornerShape = 8.dp,
                enabled = !isAddingRecording
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

        if (isAddingRecording) {
            NewRecordingCard(
                isRecording = isRecording,
                recordingSeconds = recordingSeconds,
                canAdd = !isRecording && recordingSeconds > 0,
                labelState = newRecordingLabelState,
                onToggleRecording = onToggleRecording,
                onAddClick = onAddNewRecordingClick,
                onCloseClick = onCloseNewRecordingClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailRecordingListPreview() {
    SongScribeTheme {
        DetailRecordingsList(
            recordings = listOf(
                RecordingItemUi(
                    id = "1",
                    titleState = TextFieldState("First take"),
                    filePath = "",
                    durationSeconds = 42,
                    currentDuration = 15,
                    isPrimary = true
                ),
                RecordingItemUi(
                    id = "2",
                    titleState = TextFieldState("Take 2"),
                    filePath = "",
                    durationSeconds = 20,
                    isPrimary = false
                )
            ),
            isAddingRecording = true,
            newRecordingLabelState = rememberTextFieldState()
        )
    }
}
