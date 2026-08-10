package com.song.demos.presentation.addnewdemo

import androidx.compose.foundation.text.input.TextFieldState
import com.song.core.presentation.designsystem.theme.LabelLavender
import com.song.core.presentation.designsystem.theme.LabelLightGreen
import com.song.core.presentation.designsystem.theme.LabelPink
import com.song.core.presentation.designsystem.theme.LabelRose
import com.song.core.presentation.designsystem.theme.LabelSageGreen
import com.song.core.presentation.designsystem.theme.LabelSkyBlue
import com.song.core.presentation.designsystem.theme.LabelYellow
import com.song.demos.presentation.demos.model.ColorModel
import com.song.demos.presentation.demos.model.TagModel

data class AddNewDemoState(
    val sections: List<NewDemoSections> = NewDemoSections.default,
    val colorOptions: List<ColorModel> = defaultColorOptions,
    val tagOptions: List<TagModel> = emptyList(),
    val isRecording: Boolean = false,
    val titleTextState: TextFieldState = TextFieldState(),
    val lyricsTextState: TextFieldState = TextFieldState(),
    val newTagTextState: TextFieldState = TextFieldState(),
    val showAddTagSection: Boolean = false
)

private val defaultColorOptions = listOf(
    ColorModel(LabelLavender),
    ColorModel(LabelRose),
    ColorModel(LabelSageGreen),
    ColorModel(LabelYellow),
    ColorModel(LabelSkyBlue),
    ColorModel(LabelPink),
    ColorModel(LabelLightGreen)
)
