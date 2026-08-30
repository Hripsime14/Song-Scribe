package com.song.demos.presentation.demodetail

import androidx.compose.foundation.text.input.TextFieldState
import com.song.demos.presentation.addnewdemo.defaultColorOptions
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi
import com.song.demos.presentation.demos.model.ColorModel
import com.song.demos.presentation.demos.model.TagModel
import java.util.Collections

data class DemoDetailsState(
    val sections: List<DemoDetailsSections> = DemoDetailsSections.default,
    val colorOptions: List<ColorModel> = defaultColorOptions,
    val tagOptions: List<TagModel> = Collections.emptyList(),
    val recordings: List<RecordingItemUi> = emptyList(),
    val titleTextState: TextFieldState = TextFieldState(),
    val lyricsTextState: TextFieldState = TextFieldState(),
    val newTagTextState: TextFieldState = TextFieldState(),
    val showColorSection: Boolean = false,
    val showTagSection: Boolean = false,
    val showAddTagSection: Boolean = false,
    val isSaving: Boolean = false,
    val isRecording: Boolean = false
)