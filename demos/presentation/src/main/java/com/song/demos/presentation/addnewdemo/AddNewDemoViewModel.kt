package com.song.demos.presentation.addnewdemo

import androidx.lifecycle.ViewModel
import com.song.demos.presentation.demos.mapper.toTagModels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddNewDemoViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddNewDemoState())
    val state = _state.asStateFlow()

        fun onAction(action: AddNewDemoAction) {
        when (action) {
            is AddNewDemoAction.OnTagOptionsLoaded -> _state.update { state ->
                state.copy(tagOptions = action.tags.toTagModels())
            }

            is AddNewDemoAction.OnColorSelect -> _state.update { state ->
                state.copy(
                    colorOptions  = state.colorOptions.map { it.copy(isSelected = it.color == action.color.color) }
                )
            }

            is AddNewDemoAction.OnTagClick -> _state.update { state ->
                state.copy(
                    tagOptions = state.tagOptions.map {
                        if (it.name == action.tag.name) {
                            it.copy(isSelected = !(it.isSelected))
                        } else {
                            it
                        }
                    }
                )
            }

            AddNewDemoAction.OnToggleRecording -> _state.update { state ->
                state.copy(isRecording = !state.isRecording)
            }

            AddNewDemoAction.OnAddCustomTagClick -> Unit
            AddNewDemoAction.OnCustomTagClick -> {
                _state.update { state ->
                    state.copy(showAddTagSection = !state.showAddTagSection)
                }
            }
        }
    }
}
