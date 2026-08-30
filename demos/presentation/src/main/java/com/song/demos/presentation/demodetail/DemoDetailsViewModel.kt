package com.song.demos.presentation.demodetail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.song.demos.domain.repo.DemoDetailsRepo
import com.song.demos.domain.repo.model.Demo
import com.song.demos.presentation.demodetail.mapper.toRecordingItemUi
import com.song.demos.presentation.demos.model.TagModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DemoDetailsViewModel(
    private val demoDetailsRepo: DemoDetailsRepo
): ViewModel() {

    private val _state = MutableStateFlow(DemoDetailsState())
    val state = _state.asStateFlow()

    fun onAction(action: DemoDetailsAction) {
        when(action) {
            DemoDetailsAction.OnAddCustomTagClick -> {

            }
            is DemoDetailsAction.OnAddNewRecording -> {

            }
            DemoDetailsAction.OnCloseNewRecordingSection -> {

            }
            DemoDetailsAction.OnColorIconClick -> {

            }
            is DemoDetailsAction.OnColorSelect -> {

            }
            DemoDetailsAction.OnCustomTagClick -> {

            }
            is DemoDetailsAction.OnDeleteRecording -> {

            }
            DemoDetailsAction.OnNewRecordingClick -> {

            }
            DemoDetailsAction.OnSaveDemoClick -> {

            }
            is DemoDetailsAction.OnSetPrimaryRecording -> {

            }
            is DemoDetailsAction.OnTagClick -> {

            }
            DemoDetailsAction.OnTagCloseClick -> {

            }
            DemoDetailsAction.OnTagIconClick -> {

            }
            is DemoDetailsAction.OnTogglePlayback -> {

            }
            is DemoDetailsAction.OnToggleRecording -> {

            }
        }
    }

    fun getDemoDetails(demoId: String) {
        viewModelScope.launch {
            val demo = demoDetailsRepo.getDemoDetails(demoId).first() ?: return@launch
            applyDemo(demo)
        }
    }

    private fun applyDemo(demo: Demo) {
        _state.update { state ->
            val selectedGenreTags = demo.genres.map { genre -> TagModel(name = genre, isSelected = true) }
            val mergedTagOptions = state.tagOptions.map { tag ->
                tag.copy(isSelected = demo.genres.any { it.equals(tag.name, ignoreCase = true) })
            } + selectedGenreTags.filterNot { newTag ->
                state.tagOptions.any { it.name.equals(newTag.name, ignoreCase = true) }
            }

            state.copy(
                titleTextState = TextFieldState(demo.title),
                lyricsTextState = TextFieldState(demo.lyrics),
                colorOptions = state.colorOptions.map {
                    it.copy(isSelected = it.color.toArgb().toLong() == demo.colorLabel)
                },
                tagOptions = mergedTagOptions,
                recordings = demo.recordings.map { it.toRecordingItemUi() }
            )
        }
    }

}