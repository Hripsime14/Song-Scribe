package com.song.demos.presentation.demos.model

import androidx.compose.ui.graphics.Color

data class DemoModel(
    val id: String,
    val title: String,
    val date: String,
    val colorLabel: Color,
    val recordings: List<RecordingItem>,
    val genres: List<String>,
    val moreRecordingCount: Int,
)

data class RecordingItem(
    val id: String,
    val title: String,
    val duration: Int,
    val currentDuration: Int,
    val isPrimary: Boolean,
    val recording: String
)