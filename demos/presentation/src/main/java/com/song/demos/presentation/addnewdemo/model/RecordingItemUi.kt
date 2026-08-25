package com.song.demos.presentation.addnewdemo.model

import androidx.compose.foundation.text.input.TextFieldState

data class RecordingItemUi(
    val id: String,
    val titleState: TextFieldState,
    val filePath: String,
    val durationSeconds: Int,
    val isPrimary: Boolean,
    val currentDuration: Int = 0,
    val isPlaying: Boolean = false
)
