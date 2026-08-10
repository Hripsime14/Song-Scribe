package com.song.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.theme.SongScribeTheme

@Composable
fun SongScribeColorLabel(
    modifier: Modifier = Modifier,
    color: Color,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(64.dp))
            .background(color)
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(32.dp)
            )
    )
}

@Preview
@Composable
private fun SongScribeColorLabelPreview() {
    SongScribeTheme {
        SongScribeColorLabel(
            color = Color.Red
        )
    }
}