package com.song.demos.presentation.demodetail.mapper

import androidx.compose.foundation.text.input.TextFieldState
import com.song.demos.domain.repo.model.Recording
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi

fun Recording.toRecordingItemUi(): RecordingItemUi = RecordingItemUi(
    id = id,
    titleState = TextFieldState(title),
    filePath = filePath,
    durationSeconds = (durationMillis / 1000).toInt(),
    isPrimary = isPrimary
)
