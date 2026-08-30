package com.song.demos.presentation.demos

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.song.demos.domain.repo.DemosRepo
import com.song.demos.presentation.demos.mapper.toDemoModel
import com.song.demos.presentation.demos.model.RecordingUi
import com.song.demos.presentation.demos.player.DemoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.ceil

class DemosScreenViewModel(
    private val demosRepo: DemosRepo,
    private val demoPlayer: DemoPlayer
) : ViewModel() {

    val searchState = TextFieldState()

    private val _state = MutableStateFlow(DemosScreenState(isLoading = true))
    val state = _state.asStateFlow()

    private val eventChannel = Channel<DemosScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    private var progressJob: Job? = null
    private var playingDemoId: String? = null

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
                    eventChannel.send(DemosScreenEvent.OpenDemoDetailEvent(action.demoId))
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

            is DemosScreenAction.onTogglePlayClick -> togglePlayback(action.demoId)
        }
    }

    private fun togglePlayback(demoId: String) {
        val demo = _state.value.demos.firstOrNull { it.id == demoId } ?: return
        val recording = demo.recording ?: return

        if (playingDemoId == demoId && demoPlayer.isPlaying) {
            demoPlayer.pause()
            progressJob?.cancel()
            updateRecording(demoId) { it.copy(isPlaying = false, currentDuration = positionSeconds(it.duration)) }
            return
        }

        val previousPlayingId = playingDemoId
        if (previousPlayingId != null && previousPlayingId != demoId) {
            updateRecording(previousPlayingId) { it.copy(isPlaying = false, currentDuration = positionSeconds(it.duration)) }
        }

        demoPlayer.play(
            filePath = recording.recording,
            startPositionMillis = recording.currentDuration * 1000
        ) { onPlaybackComplete(demoId) }
        playingDemoId = demoId

        updateRecording(demoId) { it.copy(isPlaying = true) }
        startProgressTicker(demoId)
    }

    private fun startProgressTicker(demoId: String) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                updateRecording(demoId) { it.copy(currentDuration = positionSeconds(it.duration)) }
                delay(PROGRESS_TICK_MILLIS)
            }
        }
    }

    private fun onPlaybackComplete(demoId: String) {
        progressJob?.cancel()
        playingDemoId = null
        updateRecording(demoId) { it.copy(isPlaying = false, currentDuration = 0) }
    }

    private fun positionSeconds(durationSeconds: Int): Int {
        return ceil(demoPlayer.currentPositionMillis / 1000.0).toInt().coerceIn(0, durationSeconds)
    }

    private fun updateRecording(demoId: String, transform: (RecordingUi) -> RecordingUi) {
        _state.update { state ->
            state.copy(
                demos = state.demos.map { demo ->
                    if (demo.id != demoId) return@map demo
                    val rec = demo.recording ?: return@map demo
                    demo.copy(recording = transform(rec))
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        demoPlayer.stop()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 300L
        private const val PROGRESS_TICK_MILLIS = 250L
    }
}