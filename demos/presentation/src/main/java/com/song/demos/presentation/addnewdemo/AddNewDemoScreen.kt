package com.song.demos.presentation.addnewdemo

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.song.core.presentation.designsystem.components.SongScribeToolbar
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.ObserveAsEvents
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.components.ColorLabelSection
import com.song.demos.presentation.addnewdemo.components.DemoTitleSection
import com.song.demos.presentation.addnewdemo.components.InfoSection
import com.song.demos.presentation.addnewdemo.components.LyricsSection
import com.song.demos.presentation.addnewdemo.components.RecordingSection
import com.song.demos.presentation.addnewdemo.components.TagSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNewDemoScreenRoot(
    onSaveChanges: () -> Unit,
    onDemoCreated: () -> Unit,
    viewModel: AddNewDemoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tagOptions = stringArrayResource(R.array.instruments).toList()
    val context = LocalContext.current

    LaunchedEffect(tagOptions) {
        viewModel.onAction(AddNewDemoAction.OnTagOptionsLoaded(tagOptions))
    }

    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            AddNewDemoEvent.DemoCreated -> onDemoCreated()
            is AddNewDemoEvent.DemoCreationFailed -> Toast.makeText(
                context,
                event.message ?: context.getString(R.string.demo_creation_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onAction(AddNewDemoAction.OnToggleRecording)
        }
    }

    AddNewDemoScreen(
        state = state,
        onAction = { action ->
            val needsPermission = action == AddNewDemoAction.OnToggleRecording &&
                    !state.isRecording &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED

            if (needsPermission) {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                viewModel.onAction(action)
            }
        },
        onCloseClick = onSaveChanges
    )
}

@Composable
fun AddNewDemoScreen(
    state: AddNewDemoState,
    modifier: Modifier = Modifier,
    onAction: (AddNewDemoAction) -> Unit = {},
    onCloseClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongScribeToolbar(
                title = stringResource(R.string.new_demo),
                showCloseButton = true,
                onCloseClick = onCloseClick,
                endButton = {
                    val canCreate = state.titleTextState.text.isNotBlank() &&
                            state.recordings.isNotEmpty() &&
                            !state.isSaving
                    TextButton(
                        onClick = { onAction(AddNewDemoAction.OnCreateDemoClick) },
                        enabled = canCreate
                    ) {
                        Text(
                            text = stringResource(R.string.create),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    ) { padding ->
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            state.sections.forEach { section ->
                when (section) {
                    NewDemoSections.Recording -> RecordingSection(
                        isRecording = state.isRecording,
                        recordingSeconds = state.recordingSeconds,
                        recordings = state.recordings,
                        onToggleRecording = {
                            onAction(AddNewDemoAction.OnToggleRecording)
                        },
                        onPlayPauseRecording = { recordingId ->
                            onAction(AddNewDemoAction.OnTogglePlayback(recordingId))
                        },
                        onSetPrimaryRecording = { recordingId ->
                            onAction(AddNewDemoAction.OnSetPrimaryRecording(recordingId))
                        },
                        onDeleteRecording = { recordingId ->
                            onAction(AddNewDemoAction.OnDeleteRecording(recordingId))
                        }
                    )

                    NewDemoSections.DemoTitle -> DemoTitleSection(
                        titleState = state.titleTextState
                    )

                    NewDemoSections.ColorLabel -> ColorLabelSection(
                        colors = state.colorOptions,
                        onColorSelect = { color ->
                            onAction(AddNewDemoAction.OnColorSelect(color))
                        }
                    )

                    NewDemoSections.Tags -> TagSection(
                        tags = state.tagOptions,
                        onTagClick = { tag ->
                            onAction(AddNewDemoAction.OnTagClick(tag))
                        },
                        onAddCustomTag = {
                            onAction(AddNewDemoAction.OnAddCustomTagClick)
                        },
                        onCustomTagClick = {
                            onAction(AddNewDemoAction.OnCustomTagClick)
                        },
                        newTagState = state.newTagTextState,
                        addNewTag = state.showAddTagSection
                    )

                    NewDemoSections.Lyrics -> LyricsSection(
                        lyricsState = state.lyricsTextState
                    )

                    NewDemoSections.Info -> InfoSection()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddNewDemoScreenPreview() {
    SongScribeTheme {
        AddNewDemoScreen(
            state = AddNewDemoState(
                tagOptions = emptyList()
            )
        )
    }
}