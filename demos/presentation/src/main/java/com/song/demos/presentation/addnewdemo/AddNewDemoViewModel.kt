package com.song.demos.presentation.addnewdemo

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.song.demos.domain.repo.NewDemoRepo
import com.song.demos.domain.repo.model.Demo
import com.song.demos.domain.repo.model.Recording
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi
import com.song.demos.presentation.addnewdemo.recorder.AudioRecorder
import com.song.demos.presentation.demos.mapper.toTagModels
import com.song.demos.presentation.demos.model.TagModel
import com.song.demos.presentation.demos.player.DemoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import kotlin.math.ceil

class AddNewDemoViewModel(
    application: Application,
    private val newDemoRepo: NewDemoRepo,
    private val demoPlayer: DemoPlayer
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AddNewDemoState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<AddNewDemoEvent>()
    val events = eventChannel.receiveAsFlow()

    private val audioRecorder = AudioRecorder(application)
    private var timerJob: Job? = null

    private var progressJob: Job? = null
    private var playingRecordingId: String? = null

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

            AddNewDemoAction.OnToggleRecording -> {
                if (_state.value.isRecording) stopRecording() else startRecording()
            }

            is AddNewDemoAction.OnDeleteRecording -> deleteRecording(action.recordingId)

            is AddNewDemoAction.OnTogglePlayback -> togglePlayback(action.recordingId)

            is AddNewDemoAction.OnSetPrimaryRecording -> setPrimaryRecording(action.recordingId)

            AddNewDemoAction.OnAddCustomTagClick -> {
                val newTagName = _state.value.newTagTextState.text.toString().trim()
                if (newTagName.isBlank()) return

                _state.update { state ->
                    val alreadyExists = state.tagOptions.any { it.name.equals(newTagName, ignoreCase = true) }
                    state.copy(
                        tagOptions = if (alreadyExists) {
                            state.tagOptions.map {
                                if (it.name.equals(newTagName, ignoreCase = true)) it.copy(isSelected = true) else it
                            }
                        } else {
                            state.tagOptions + TagModel(name = newTagName, isSelected = true)
                        },
                        showAddTagSection = false
                    )
                }
                _state.value.newTagTextState.clearText()
            }
            AddNewDemoAction.OnCustomTagClick -> {
                _state.update { state ->
                    state.copy(showAddTagSection = !state.showAddTagSection)
                }
            }

            AddNewDemoAction.OnCreateDemoClick -> createDemo()
        }
    }

    private fun startRecording() {
        val file = runCatching { audioRecorder.start() }.getOrNull() ?: return

        _state.update { state ->
            state.copy(
                isRecording = true,
                recordingSeconds = 0,
                recordingFilePath = file.absolutePath
            )
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(ONE_SECOND_MILLIS)
                _state.update { state -> state.copy(recordingSeconds = state.recordingSeconds + 1) }
            }
        }
    }

    private fun stopRecording() {
        timerJob?.cancel()
        timerJob = null
        val file = audioRecorder.stop() ?: return
        val currentState = _state.value

        val takeTitle = if (currentState.recordings.isEmpty()) {
            getApplication<Application>().getString(R.string.first_take)
        } else {
            getApplication<Application>().getString(R.string.take_number, currentState.recordings.size + 1)
        }
        val newRecording = RecordingItemUi(
            id = UUID.randomUUID().toString(),
            titleState = TextFieldState(takeTitle),
            filePath = file.absolutePath,
            durationSeconds = currentState.recordingSeconds,
            isPrimary = currentState.recordings.isEmpty()
        )

        _state.update { state ->
            state.copy(
                isRecording = false,
                recordingSeconds = 0,
                recordingFilePath = null,
                recordings = state.recordings + newRecording
            )
        }
    }

    private fun deleteRecording(recordingId: String) {
        val currentState = _state.value
        val removed = currentState.recordings.firstOrNull { it.id == recordingId } ?: return

        if (playingRecordingId == recordingId) {
            progressJob?.cancel()
            demoPlayer.stop()
            playingRecordingId = null
        }

        File(removed.filePath).delete()

        val remaining = currentState.recordings.filter { it.id != recordingId }
        val updatedRecordings = if (removed.isPrimary) {
            remaining.mapIndexed { index, item -> item.copy(isPrimary = index == 0) }
        } else {
            remaining
        }

        _state.update { state -> state.copy(recordings = updatedRecordings) }
    }

    private fun setPrimaryRecording(recordingId: String) {
        _state.update { state ->
            state.copy(
                recordings = state.recordings.map { it.copy(isPrimary = it.id == recordingId) }
            )
        }
    }

    private fun togglePlayback(recordingId: String) {
        val recording = _state.value.recordings.firstOrNull { it.id == recordingId } ?: return

        if (playingRecordingId == recordingId && demoPlayer.isPlaying) {
            demoPlayer.pause()
            progressJob?.cancel()
            updateRecordingItem(recordingId) { it.copy(isPlaying = false, currentDuration = positionSeconds(it.durationSeconds)) }
            return
        }

        val previousPlayingId = playingRecordingId
        if (previousPlayingId != null && previousPlayingId != recordingId) {
            updateRecordingItem(previousPlayingId) { it.copy(isPlaying = false, currentDuration = positionSeconds(it.durationSeconds)) }
        }

        demoPlayer.play(
            filePath = recording.filePath,
            startPositionMillis = recording.currentDuration * 1000
        ) { onPlaybackComplete(recordingId) }
        playingRecordingId = recordingId

        updateRecordingItem(recordingId) { it.copy(isPlaying = true) }
        startProgressTicker(recordingId)
    }

    private fun startProgressTicker(recordingId: String) {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                updateRecordingItem(recordingId) { it.copy(currentDuration = positionSeconds(it.durationSeconds)) }
                delay(PROGRESS_TICK_MILLIS)
            }
        }
    }

    private fun onPlaybackComplete(recordingId: String) {
        progressJob?.cancel()
        playingRecordingId = null
        updateRecordingItem(recordingId) { it.copy(isPlaying = false, currentDuration = 0) }
    }

    private fun positionSeconds(durationSeconds: Int): Int {
        return ceil(demoPlayer.currentPositionMillis / 1000.0).toInt().coerceIn(0, durationSeconds)
    }

    private fun updateRecordingItem(recordingId: String, transform: (RecordingItemUi) -> RecordingItemUi) {
        _state.update { state ->
            state.copy(
                recordings = state.recordings.map { item ->
                    if (item.id == recordingId) transform(item) else item
                }
            )
        }
    }

    private fun createDemo() {
        val currentState = _state.value
        if (currentState.isSaving) return
        if (currentState.isRecording) stopRecording()

        val recordings = _state.value.recordings
        if (recordings.isEmpty()) return
        val title = currentState.titleTextState.text.toString().trim()
        if (title.isBlank()) return

        val createdAtMillis = System.currentTimeMillis()
        val selectedColor = currentState.colorOptions.firstOrNull { it.isSelected }
            ?: currentState.colorOptions.first()
        val demo = Demo(
            id = UUID.randomUUID().toString(),
            title = title,
            createdAtMillis = createdAtMillis,
            colorLabel = selectedColor.color.toArgb().toLong(),
            genres = currentState.tagOptions.filter { it.isSelected }.map { it.name },
            lyrics = currentState.lyricsTextState.text.toString().trim(),
            recordings = recordings.map { item ->
                Recording(
                    id = item.id,
                    title = item.titleState.text.toString().trim(),
                    durationMillis = item.durationSeconds * ONE_SECOND_MILLIS,
                    filePath = item.filePath,
                    isPrimary = item.isPrimary,
                    createdAtMillis = createdAtMillis
                )
            }
        )

        viewModelScope.launch {
            _state.update { state -> state.copy(isSaving = true) }
            val result = runCatching { newDemoRepo.createDemo(demo) }
            _state.update { state -> state.copy(isSaving = false) }
            result
                .onSuccess { eventChannel.send(AddNewDemoEvent.DemoCreated) }
                .onFailure { throwable ->
                    eventChannel.send(AddNewDemoEvent.DemoCreationFailed(throwable.message))
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        audioRecorder.stop()
        progressJob?.cancel()
        demoPlayer.stop()
    }

    companion object {
        private const val ONE_SECOND_MILLIS = 1000L
        private const val PROGRESS_TICK_MILLIS = 250L
    }
}
