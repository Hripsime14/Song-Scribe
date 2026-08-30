package com.song.demos.presentation.demodetail

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.song.core.presentation.designsystem.components.SongScribeToolbar
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.AddNewDemoAction
import com.song.demos.presentation.addnewdemo.components.DemoTitleSection
import com.song.demos.presentation.addnewdemo.components.InfoSection
import com.song.demos.presentation.addnewdemo.components.LyricsSection
import com.song.demos.presentation.demodetail.components.DetailRecordingsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun DemoDetailsScreenRoot(
    demoId: String,
    onSaveChanges: () -> Unit,
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
        onSaveClick = onSaveChanges
    )
}

@Composable
fun DemoDetailsScreen(
    state: DemoDetailsState,
    modifier: Modifier = Modifier,
    onAction: (DemoDetailsAction) -> Unit = {},
    onSaveClick: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SongScribeToolbar(
                title = stringResource(R.string.demo_details),
                showBackButton = true,
                onCloseClick = onSaveClick,
                onBackClick = {},
                endButton = {
                    val canCreate = state.titleTextState.text.isNotBlank() &&
                            state.recordings.isNotEmpty() &&
                            !state.isSaving
                    TextButton(
                        onClick = { onAction(DemoDetailsAction.OnSaveDemoClick) },
                        enabled = canCreate
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = padding
        ) {
            items(state.sections, {it.toString()}) {
                when(it) {
                    DemoDetailsSections.DateAndGenre -> {}
                    DemoDetailsSections.DemoTitle -> DemoTitleSection(
                        titleState = state.titleTextState
                    )
                    DemoDetailsSections.Recordings -> {
                        DetailRecordingsList(recordings = state.recordings)
                    }
                    DemoDetailsSections.Info -> InfoSection()
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
            onSaveClick = TODO()
        )
    }
}