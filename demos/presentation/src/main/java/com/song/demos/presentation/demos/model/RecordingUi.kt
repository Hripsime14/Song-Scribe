package com.song.demos.presentation.demos.model

data class RecordingUi(
    val id: String,
    val title: String,
    val duration: Int,
    val currentDuration: Int,
    val isPrimary: Boolean,
    val recording: String
)