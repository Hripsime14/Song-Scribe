package com.song.demos.presentation.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.song.core.presentation.designsystem.components.SongScribeToolbar
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.ObserveAsEvents
import com.song.demos.presentation.R
import com.song.demos.presentation.demos.components.DemoListItem
import com.song.demos.presentation.demos.components.DemoSearchBar
import com.song.demos.presentation.demos.components.SettingsBottomSheet
import com.song.demos.presentation.demos.model.DemoUi
import com.song.demos.presentation.demos.model.RecordingUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun DemosScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: DemosScreenViewModel = koinViewModel(),
    onNavigateToAddNewDemo: () -> Unit,
    onNavigateToDemoDetails: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            DemosScreenEvent.AddDemoEvent -> onNavigateToAddNewDemo()
            is DemosScreenEvent.OpenDemoDetailEvent -> {
                onNavigateToDemoDetails(event.demoId)
            }
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
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        // The search bar is the first focusable field on screen, and Android's default
        // View-focus behavior can leave it (and only it, not the keyboard, thanks to
        // windowSoftInputMode="stateHidden") focused right on launch. Clear it explicitly.
        focusManager.clearFocus(force = true)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongScribeToolbar(
                title = stringResource(R.string.my_demos),
                showSettingsButton = true,
                onSettingsClick = { onAction(DemosScreenAction.onSettingsClick) }
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
                val deletingDemoTitle = state.demos.find { it.id == state.deletingDemoId }?.title.orEmpty()
                AlertDialog(
                    onDismissRequest = { onAction(DemosScreenAction.onDismissDeleteDialog) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.65f)
                        )
                    },
                    title = {
                        Text(text = stringResource(R.string.delete_demo_title))
                    },
                    text = {
                        Text(text = stringResource(R.string.delete_demo_message, deletingDemoTitle))
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { onAction(DemosScreenAction.onAcceptDeleteDialog) }
                        ) {
                            Text(
                                text = stringResource(R.string.delete),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { onAction(DemosScreenAction.onDismissDeleteDialog) }
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
            if (state.showSettingsSheet) {
                SettingsBottomSheet(
                    currentThemeMode = state.themeMode,
                    currentLanguage = state.language,
                    onThemeModeSelected = { themeMode ->
                        onAction(DemosScreenAction.onThemeModeSelected(themeMode))
                    },
                    onLanguageSelected = { language ->
                        onAction(DemosScreenAction.onLanguageSelected(language))
                    },
                    onDismiss = { onAction(DemosScreenAction.onDismissSettingsSheet) }
                )
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

                    state.demos.isEmpty() -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_demos_yet),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

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
                                onItemClick = {
                                    onAction(DemosScreenAction.onItemClick(demoId = demo.id))
                                }
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