package com.song.demos.presentation.demos.model

import androidx.compose.ui.graphics.Color

data class DemoUi(
    val id: String,
    val title: String,
    val date: String,
    val colorLabel: Color,
    val recording: RecordingUi?,
    val genres: List<String>,
    val moreRecordingCount: Int,
)