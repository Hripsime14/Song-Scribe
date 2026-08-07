package com.song.demos.presentation.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.song.core.presentation.designsystem.components.SongScribeToolbar
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R
import com.song.demos.presentation.demos.components.DemoListItem
import com.song.demos.presentation.demos.components.DemoSearchBar
import com.song.demos.presentation.demos.model.DemoUi
import com.song.demos.presentation.demos.model.RecordingUi

@Composable
fun DemosScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateToDetail: () -> Unit
) {
    DemosScreen(modifier = modifier.fillMaxSize())
}

@Composable
fun DemosScreen(
    modifier: Modifier = Modifier,
) {
    val searchState = rememberTextFieldState()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongScribeToolbar(
                title = stringResource(R.string.my_demos),
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DemoSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    state = searchState
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.recordings, 2),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            DemoListItem(
                modifier = Modifier,
                demoUi = DemoUi(
                    id = "1",
                    title = "Yellow Stone",
                    date = "21/07/2022",
                    colorLabel = MaterialTheme.colorScheme.tertiaryFixed,
                    recordings = listOf(
                        RecordingUi(
                            id = "1",
                            title = "First Take",
                            duration = 33,
                            isPrimary = true,
                            recording = "",
                            currentDuration = 15,
                        ),
                        RecordingUi(
                            id = "2",
                            title = "Second Take",
                            duration = 40,
                            isPrimary = false,
                            recording = "",
                            currentDuration = 15,
                        )
                    ),
                    genres = listOf("piano", "guitar", "drums", "piano", "guitar", "drums", "piano", "guitar", "drums"),
                    moreRecordingCount = 2
                )
            )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoScreenPreview() {
    SongScribeTheme {
        DemosScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}