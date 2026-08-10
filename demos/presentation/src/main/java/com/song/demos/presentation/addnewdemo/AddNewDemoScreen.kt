package com.song.demos.presentation.addnewdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.song.core.presentation.designsystem.components.SongScribeToolbar
import com.song.core.presentation.designsystem.theme.SongScribeTheme
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.components.ColorLabelSection
import com.song.demos.presentation.addnewdemo.components.DemoTitleSection
import com.song.demos.presentation.addnewdemo.components.LyricsSection
import com.song.demos.presentation.addnewdemo.components.RecordingSection
import com.song.demos.presentation.addnewdemo.components.TagSection

@Composable
fun AddNewDemoScreenRoot(
    onSaveChanges: () -> Unit,
    viewModel: AddNewDemoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tagOptions = stringArrayResource(R.array.instruments).toList()

    LaunchedEffect(tagOptions) {
        viewModel.onAction(AddNewDemoAction.OnTagOptionsLoaded(tagOptions))
    }

    AddNewDemoScreen(
        state = state,
        onAction = viewModel::onAction,
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
                onCloseClick = onCloseClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            state.sections.forEach { section ->
                when (section) {
                    NewDemoSections.Recording -> RecordingSection(
                        isRecording = state.isRecording,
                        onToggleRecording = {
                            onAction(AddNewDemoAction.OnToggleRecording)
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