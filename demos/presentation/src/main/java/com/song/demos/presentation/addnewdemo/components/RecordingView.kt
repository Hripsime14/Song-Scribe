package com.song.demos.presentation.addnewdemo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeButton
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.formatAsDuration
import com.song.demos.presentation.R

@Composable
fun RecordingView(
    modifier: Modifier = Modifier,
    isRecording: Boolean = false,
    hasRecording: Boolean = false,
    recordingSeconds: Int = 0,
    onToggleRecording: () -> Unit = {},
    onDiscardRecording: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SongScribeButton(
            onClick = onToggleRecording,
            icon = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
            tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            iconSize = 48.dp,
            contentPadding = 20.dp
        )
        Text(
            modifier = Modifier
                .addDefaultTopPadding(),
            text = recordingSeconds.formatAsDuration(),
            style = MaterialTheme.typography.headlineSmall,
            color = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.scrim,
        )
        Text(
            modifier = Modifier
                .addDefaultTopPadding(),
            text = when {
                isRecording -> stringResource(R.string.recording)
                hasRecording -> stringResource(R.string.ready)
                else -> stringResource(R.string.tap_to_record)
            },
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
        )
        val canDiscard = !isRecording && hasRecording
        Text(
            modifier = Modifier
                .addDefaultTopPadding()
                .addDefaultTopPadding()
                .alpha(if (canDiscard) 1f else 0f)
                .clickable(enabled = canDiscard, onClick = onDiscardRecording),
            text = stringResource(R.string.discard_and_rerecord),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Preview
@Composable
private fun RecordingViewPreview() {
    SongScribeTheme {
        RecordingView()
    }
}