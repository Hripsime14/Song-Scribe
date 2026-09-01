package com.song.demos.presentation.demodetail

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.song.core.presentation.designsystem.components.SongScribePositiveButton
import com.song.core.presentation.designsystem.components.SongScribeToolbar
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.core.presentation.ui.util.countWords
import com.song.core.presentation.ui.util.formatDate
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.components.DemoTitleSection
import com.song.demos.presentation.addnewdemo.components.InfoSection
import com.song.demos.presentation.addnewdemo.components.LyricsSection
import com.song.demos.presentation.demodetail.components.DetailRecordingsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun DemoDetailsScreenRoot(
    demoId: String,
    onSaveChanges: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: DemoDetailsViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val tagOptions = stringArrayResource(R.array.instruments).toList()
    val context = LocalContext.current

    LaunchedEffect(demoId) {
        viewModel.getDemoDetails(demoId)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onAction(DemoDetailsAction.OnToggleRecording)
        }
    }

    DemoDetailsScreen(
        state = state,
        onAction = { action ->
            val needsPermission = action == DemoDetailsAction.OnToggleRecording &&
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
        onSaveClick = onSaveChanges,
        onBackClick = onBackClick
    )
}

@Composable
fun DemoDetailsScreen(
    state: DemoDetailsState,
    modifier: Modifier = Modifier,
    onAction: (DemoDetailsAction) -> Unit = {},
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongScribeToolbar(
                title = stringResource(R.string.demo_details),
                showBackButton = true,
                onBackClick = onBackClick,
                endButton = {
                    val canCreate = state.titleTextState.text.isNotBlank() &&
                            state.recordings.isNotEmpty() &&
                            !state.isSaving
                    SongScribePositiveButton(
                        onClick = {
                            onAction(DemoDetailsAction.OnSaveDemoClick)
                            onSaveClick()
                        },
                        enabled = canCreate,
                        text = stringResource(R.string.save),
                        containerColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        icon = Icons.Default.Save
                    )
                }
            )
        }
    ) { padding ->

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = padding.calculateBottomPadding()
            )
        ) {
            items(state.sections, {it.toString()}) {
                when(it) {
                    DemoDetailsSections.DateAndGenre -> {}
                    DemoDetailsSections.DemoTitle -> DemoTitleSection(
                        titleState = state.titleTextState
                    )
                    DemoDetailsSections.Recordings -> {
                        DetailRecordingsList(
                            recordings = state.recordings,
                            isAddingRecording = state.isAddingRecording,
                            isRecording = state.isRecording,
                            recordingSeconds = state.recordingSeconds,
                            newRecordingLabelState = state.newRecordingLabelState,
                            onNewRecordingClick = { onAction(DemoDetailsAction.OnNewRecordingClick) },
                            onCloseNewRecordingClick = { onAction(DemoDetailsAction.OnCloseNewRecordingSection) },
                            onToggleRecording = { onAction(DemoDetailsAction.OnToggleRecording) },
                            onAddNewRecordingClick = { onAction(DemoDetailsAction.OnAddNewRecordingClick) },
                            onPlayPauseRecording = { id -> onAction(DemoDetailsAction.OnTogglePlayback(id)) },
                            onSetPrimaryRecording = { id -> onAction(DemoDetailsAction.OnSetPrimaryRecording(id)) },
                            onDeleteRecording = { id -> onAction(DemoDetailsAction.OnDeleteRecording(id)) }
                        )
                    }
                    DemoDetailsSections.Info -> InfoSection(
                        createdTime = state.createdAtMillis.formatDate(),
                        wordsCount = countWords(state.lyricsTextState.text.toString())
                    )
                    DemoDetailsSections.Lyrics -> LyricsSection(
                        lyricsState = state.lyricsTextState
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun DemoDetailsScreenPreview() {
    SongScribeTheme {
        DemoDetailsScreen(
            state = DemoDetailsState(),
            modifier = TODO(),
            onAction = TODO(),
            onSaveClick = TODO(),
            onBackClick = {}
        )
    }
}