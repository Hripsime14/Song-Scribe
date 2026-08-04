package com.song.demos.presentation.demos.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R

@Composable
fun DemoSearchBar(
    modifier: Modifier
) {
    TextField(
        modifier = modifier
            .height(45.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp)),
        state = TextFieldState(
            initialText = ""
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                modifier = Modifier.size(24.dp),
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun DemoSearchBarPreview() {
    SongScribeTheme {
        DemoSearchBar(Modifier)
    }
}
