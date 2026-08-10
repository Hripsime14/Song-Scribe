package com.song.demos.presentation.addnewdemo.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeBeige
import com.song.core.presentation.designsystem.components.SongScribeButton
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme

@Composable
fun RecordingView(
    modifier: Modifier = Modifier,
    isRecording: Boolean = false,
    onToggleRecording: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SongScribeButton(
            onClick = onToggleRecording,
            icon = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
            shape = RoundedCornerShape(32.dp)
        )
        Text(
            modifier = Modifier
                .addDefaultTopPadding(),
            text = "0:00",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = if (isRecording) "Recording..." else "Tap to record",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
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