package com.song.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.song.core.presentation.designsystem.theme.SongScribeTheme

private val BorderWidth = 2.dp
private val BorderGap = 2.dp

@Composable
fun SongScribeColorLabel(
    modifier: Modifier = Modifier,
    color: Color,
    size: Dp = 48.dp,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .size(size + 2 * (BorderGap + BorderWidth))
            .border(
                width = BorderWidth,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Preview
@Composable
private fun SongScribeColorLabelPreview() {
    SongScribeTheme {
        SongScribeColorLabel(
            color = Color.Red,
            isSelected = true
        )
    }
}