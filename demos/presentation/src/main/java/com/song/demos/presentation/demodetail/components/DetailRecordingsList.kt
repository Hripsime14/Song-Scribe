package com.song.demos.presentation.demodetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.domain.validation.DemoValidator
import com.song.core.presentation.designsystem.components.SongScribePositiveButton
import com.song.core.presentation.designsystem.extension.addDefaultStartPadding
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .addDefaultTopPadding()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                imageVector = Icons.Default.MusicNote,
                contentDescription = "Note",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .addDefaultStartPadding()
            ) {
                Text(
                    text = stringResource(R.string.recordings_section),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                val canAddMoreRecordings = DemoValidator.canAddRecording(recordings.size)
                Text(
                    text = pluralStringResource(
                        R.plurals.takes_count,
                        recordings.size,
                        recordings.size
                    ) + when {
                        !canAddMoreRecordings -> " · " + stringResource(R.string.max_recordings_reached)
                        recordings.count() > 1 -> " · " + stringResource(R.string.tap_star_to_set_primary)
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            val canAddMoreRecordings = DemoValidator.canAddRecording(recordings.size)
            SongScribePositiveButton(
                icon = Icons.Default.Add,
                text = stringResource(R.string.add),
                modifier = Modifier
                    .padding(4.dp)
                    .clickable(
                        enabled = !isAddingRecording && canAddMoreRecordings,
                        onClick = onNewRecordingClick,
                        interactionSource = null,
                        indication = null
                    ),
                containerColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = onNewRecordingClick,
                cornerShape = 8.dp,
                enabled = !isAddingRecording && canAddMoreRecordings
            )
        }

        recordings.forEach { recording ->
            key(recording.id) {
                RecordingListItem(
                    recording = recording,
                    onPlayPauseClick = { onPlayPauseRecording(recording.id) },
                    onSetPrimaryClick = { onSetPrimaryRecording(recording.id) },
                    onDeleteClick = { onDeleteRecording(recording.id) },
                    showDeleteButton = recordings.size > 1
                )
            }
        }

        AnimatedVisibility(
            visible = isAddingRecording
        ) {
            NewRecordingCard(
                isRecording = isRecording,
                recordingSeconds = recordingSeconds,
                canAdd = !isRecording && DemoValidator.isRecordingDurationValid(recordingSeconds),
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
