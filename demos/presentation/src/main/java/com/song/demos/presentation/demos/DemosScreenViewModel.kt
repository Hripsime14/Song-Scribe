package com.song.demos.presentation.demos

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.song.demos.domain.repo.DemosRepo
import com.song.demos.presentation.demos.mapper.toDemoModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DemosScreenViewModel(
    private val demosRepo: DemosRepo
) : ViewModel() {

    val searchState = TextFieldState()

    private val _state = MutableStateFlow(DemosScreenState(isLoading = true))
    val state = _state.asStateFlow()

    private val eventChannel = Channel<DemosScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        snapshotFlow { searchState.text.toString() }
            .debounce(SEARCH_DEBOUNCE_MILLIS)
            .distinctUntilChanged()
            .flatMapLatest { query -> demosRepo.searchDemos(query.trim()) }
            .catch { throwable ->
                _state.update { state ->
                    state.copy(isLoading = false, error = throwable.message)
                }
            }
            .onEach { demos ->
                _state.update { state ->
                    state.copy(
                        demos = demos.map { it.toDemoModel() },
                        demoCount = demos.size,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: DemosScreenAction) {
        when (action) {
            DemosScreenAction.onAddDemoClick -> {
                viewModelScope.launch {
                    eventChannel.send(DemosScreenEvent.AddDemoEvent)
                }
            }

            is DemosScreenAction.onItemClick -> {
                viewModelScope.launch {
                    eventChannel.send(DemosScreenEvent.OpenDemoDetailEvent)
                }
            }

            is DemosScreenAction.onDeleteRequest -> {
                _state.update { state ->
                    state.copy(showDeleteDialog = true, deletingDemoId = action.demoId)
                }
            }

            DemosScreenAction.onDismissDeleteDialog -> {
                _state.update { state ->
                    state.copy(showDeleteDialog = false, deletingDemoId = "")
                }
            }

            DemosScreenAction.onAcceptDeleteDialog -> {
                viewModelScope.launch {
                    demosRepo.deleteDemo(_state.value.deletingDemoId)
                }
                _state.update { state ->
                    state.copy(showDeleteDialog = false, deletingDemoId = "")
                }
            }

            is DemosScreenAction.onTogglePlayClick -> {
                _state.update { state ->
                    state.copy(
                        demos = state.demos.map { demo ->
                            val recording = demo.recording ?: return@map demo
                            if (demo.id == action.demoId) {
                                demo.copy(recording = recording.copy(isPlaying = !recording.isPlaying))
                            } else if (recording.isPlaying) {
                                demo.copy(recording = recording.copy(isPlaying = false))
                            } else {
                                demo
                            }
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}