package com.song.demos.presentation.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.song.core.presentation.designsystem.components.SongScribeToolbar
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.ObserveAsEvents
import com.song.demos.presentation.R
import com.song.demos.presentation.demos.components.DemoListItem
import com.song.demos.presentation.demos.components.DemoSearchBar
import com.song.demos.presentation.demos.model.DemoUi
import com.song.demos.presentation.demos.model.RecordingUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun DemosScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: DemosScreenViewModel = koinViewModel(),
    onNavigateToAddNewDemo: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            DemosScreenEvent.AddDemoEvent -> onNavigateToAddNewDemo()
            DemosScreenEvent.OpenDemoDetailEvent -> Unit
            DemosScreenEvent.OpenDeleteEvent -> Unit
        }
    }
    DemosScreen(
        modifier = modifier.fillMaxSize(),
        state = state,
        searchState = viewModel.searchState,
        onAction = viewModel::onAction
    )
}

@Composable
fun DemosScreen(
    modifier: Modifier = Modifier,
    state: DemosScreenState = DemosScreenState(),
    searchState: TextFieldState = rememberTextFieldState(),
    onAction: (DemosScreenAction) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongScribeToolbar(
                title = stringResource(R.string.my_demos),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(DemosScreenAction.onAddDemoClick) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_demo)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.showDeleteDialog) {
                Dialog(
                    onDismissRequest = { onAction(DemosScreenAction.onDismissDeleteDialog) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.are_you_sure),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                            ,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { onAction(DemosScreenAction.onAcceptDeleteDialog) }
                            ) {
                                Text(
                                    text = stringResource(R.string.yes),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Button(
                                onClick = { onAction(DemosScreenAction.onDismissDeleteDialog) }
                            ) {

                                Text(
                                    text = stringResource(R.string.no),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
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
                    text = stringResource(R.string.recordings, state.demoCount),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                when {
                    state.isLoading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    state.error != null -> Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.error,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    state.demos.isEmpty() -> Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.no_demos_yet),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 88.dp)
                    ) {
                        items(
                            items = state.demos,
                            key = { demo -> demo.id }
                        ) { demo ->
                            DemoListItem(
                                modifier = Modifier.fillMaxWidth(),
                                demoUi = demo,
                                onPlayPauseClick = {
                                    onAction(DemosScreenAction.onTogglePlayClick(demo.id, demo.recording?.isPlaying == true))
                                },
                                onDeleteClick = {
                                    onAction(DemosScreenAction.onDeleteRequest(demo.id))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DemoScreenPreview() {
    SongScribeTheme {
        DemosScreen(
            modifier = Modifier.fillMaxSize(),
            state = DemosScreenState(
                demoCount = 1,
                demos = listOf(
                    DemoUi(
                        id = "1",
                        title = "Yellow Stone",
                        date = "21/07/2022",
                        colorLabel = MaterialTheme.colorScheme.tertiaryFixed,
                        recording =
                            RecordingUi(
                                id = "1",
                                title = "First Take",
                                duration = 33,
                                isPrimary = true,
                                recording = "",
                                currentDuration = 15,
                                isPlaying = true
                            )
                        ,
                        genres = listOf("piano", "guitar", "drums"),
                        moreRecordingCount = 0
                    )
                )
            )
        )
    }
}