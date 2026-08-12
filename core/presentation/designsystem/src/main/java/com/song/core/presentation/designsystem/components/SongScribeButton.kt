package com.song.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.theme.SongScribeTheme

@Composable
fun SongScribeButton(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    icon: ImageVector,
    tint: Color = MaterialTheme.colorScheme.primary,
    iconSize: Dp = 24.dp,
    contentPadding: Dp = 8.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = tint)
            .padding(contentPadding)
            .clickable(
                onClick = onClick,
                interactionSource = null,
                indication = null
            )
    ) {
        Icon(
            icon,
            modifier = Modifier.size(iconSize),
            contentDescription = "Play",
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview
@Composable
private fun SongScribeCircleButtonPreview() {
    SongScribeTheme {
        SongScribeButton(
            icon = Icons.Default.PlayArrow,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SongScribeSquireButtonPreview() {
    SongScribeTheme {
        SongScribeButton(
            icon = Icons.Default.Add,
            shape = RoundedCornerShape(8.dp),
            onClick = {}
        )
    }
}