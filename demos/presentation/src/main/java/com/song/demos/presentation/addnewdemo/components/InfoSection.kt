package com.song.demos.presentation.addnewdemo.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R

@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    createdTime: String = "May 14, 2026",
    remainingChars: Int = 0,
    showLyricsHint: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                text = createdTime,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = pluralStringResource(R.plurals.chars_remaining, remainingChars, remainingChars),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        if (showLyricsHint) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(R.string.no_lyrics_hint),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoSectionPreview() {
    SongScribeTheme {
        InfoSection(remainingChars = 4930)
    }
}