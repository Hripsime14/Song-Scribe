package com.song.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.theme.SongScribeTheme

@Composable
fun SongScribePositiveButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    containerColor: Color,
    textColor: Color,
    hasBorder: Boolean = false,
    cornerShape: Dp = 32.dp,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(cornerShape))
            .border(
                width = 0.5.dp,
                color = if (hasBorder) MaterialTheme.colorScheme.outline else Color.Transparent,
                shape = RoundedCornerShape(cornerShape)
            )
            .background(containerColor)
            .clickable(onClick = onClick, interactionSource = null, indication = null)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = textColor
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun SongScribeCreateButtonPreview() {
    SongScribeTheme {
        SongScribePositiveButton(
            icon = Icons.Default.Check,
            text = "Create",
            containerColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SongScribeSaveButtonPreview() {
    SongScribeTheme {
        SongScribePositiveButton(
            icon = Icons.Default.Save,
            text = "Save",
            containerColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun SongScribeAddButtonPreview() {
    SongScribeTheme {
        SongScribePositiveButton(
            icon = Icons.Default.Add,
            text = "Add",
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            textColor = MaterialTheme.colorScheme.primary,
            onClick = {},
            cornerShape = 16.dp
        )
    }
}