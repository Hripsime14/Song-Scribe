package com.song.demos.presentation.demodetail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.song.demos.presentation.addnewdemo.components.RecordingListItem
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi

@Composable
fun DetailRecordingsList(
    modifier: Modifier = Modifier,
    recordings: List<RecordingItemUi>
) {
    recordings.forEach { rec ->
        RecordingListItem(recording = rec)
    }
}