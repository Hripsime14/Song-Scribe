package com.song.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.extension.addDefaultStartPadding
import com.song.core.presentation.designsystem.theme.SongScribeTheme

@Composable
fun SongScribeToolbar(
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    showCloseButton: Boolean = false,
    title: String = "Demos",
    onBackClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    endButton: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showBackButton) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier.clickable(
                    onClick = onBackClick,
                    interactionSource = null,
                    indication = null
                ),
            )
        }
        if (showCloseButton) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.clickable(
                    onClick = onCloseClick,
                    interactionSource = null,
                    indication = null
                ),
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .addDefaultStartPadding()
            ,
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )
        endButton()
    }
}

@Preview(showBackground = true)
@Composable
private fun SongScribeToolbarPreview() {
    SongScribeTheme {
        SongScribeToolbar()
    }
}