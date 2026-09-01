package com.song.demos.presentation.demodetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeButton
import com.song.core.presentation.designsystem.components.SongScribePositiveButton
import com.song.core.presentation.designsystem.extension.addDefaultTopPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.formatAsDuration
import com.song.demos.presentation.R

@Composable
fun NewRecordingCard(
    modifier: Modifier = Modifier,
    isRecording: Boolean = false,
    recordingSeconds: Int = 0,
    canAdd: Boolean = false,
    labelState: TextFieldState,
    onToggleRecording: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .addDefaultTopPadding()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                cornerRadius = 16.dp
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.new_recording),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                modifier = Modifier.clickable(
                    onClick = onCloseClick,
                    interactionSource = null,
                    indication = null
                ),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .addDefaultTopPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SongScribeButton(
                icon = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                iconSize = 28.dp,
                contentPadding = 14.dp,
                onClick = onToggleRecording
            )
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                state = labelState,
                shape = RoundedCornerShape(8.dp),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                placeholder = {
                    Text(
                        text = stringResource(R.string.new_recording_label_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .addDefaultTopPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recordingSeconds.formatAsDuration(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.scrim,
                )
                Text(
                    text = if (isRecording) {
                        stringResource(R.string.recording)
                    } else {
                        stringResource(R.string.tap_to_record)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            SongScribePositiveButton(
                modifier = Modifier.alpha(if (canAdd) 1f else 0.4f),
                icon = Icons.Default.Check,
                text = stringResource(R.string.add),
                hasBorder = true,
                textColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Transparent,
                onClick = { if (canAdd) onAddClick() }
            )
        }
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: Dp,
    strokeWidth: Dp = 1.dp
): Modifier = this.drawWithContent {
    drawContent()
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
        style = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun NewRecordingCardPreview() {
    SongScribeTheme {
        NewRecordingCard(
            labelState = rememberTextFieldState(),
            recordingSeconds = 0,
            canAdd = false
        )
    }
}
