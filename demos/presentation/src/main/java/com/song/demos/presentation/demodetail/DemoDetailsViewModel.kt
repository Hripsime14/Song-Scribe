package com.song.demos.presentation.demodetail

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.song.demos.domain.repo.DemoDetailsRepo
import com.song.demos.domain.repo.model.Demo
import com.song.demos.domain.repo.model.Recording
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi
import com.song.demos.presentation.addnewdemo.recorder.AudioRecorder
import com.song.demos.presentation.demodetail.mapper.toRecordingItemUi
import com.song.demos.presentation.demos.mapper.toTagModels
import com.song.demos.presentation.demos.model.TagModel
import com.song.demos.presentation.demos.player.DemoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import kotlin.math.ceil

class DemoDetailsViewModel(
    application: Application,
    private val demoDetailsRepo: DemoDetailsRepo,
    private val demoPlayer: DemoPlayer
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(DemoDetailsState())
    val state = _state.asStateFlow()

    private val audioRecorder = AudioRecorder(application)
    private var timerJob: Job? = null

    private var progressJob: Job? = null
    private var playingRecordingId: String? = null

    fun onAction(action: DemoDetailsAction) {
        when (action) {
            DemoDetailsAction.OnAddCustomTagClick -> addCustomTag()

            DemoDetailsAction.OnAddNewRecordingClick -> addNewRecording()
            DemoDetailsAction.OnCloseNewRecordingSection -> closeNewRecordingSection()
            DemoDetailsAction.OnColorIconClick -> {

            }

            is DemoDetailsAction.OnColorSelect -> {
                _state.update { state ->
                    state.copy(
                        colorOptions  = state.colorOptions.map { it.copy(isSelected = it.color == action.color.color) }
                    )
                }
            }

            DemoDetailsAction.OnCustomTagClick -> _state.update { state ->
                state.copy(showAddTagSection = !state.showAddTagSection)
            }

            is DemoDetailsAction.OnDeleteRecording -> deleteRecording(action.recordingId)
            DemoDetailsAction.OnNewRecordingClick -> _state.update { it.copy(isAddingRecording = true) }
            DemoDetailsAction.OnSaveDemoClick -> saveChanged()

            is DemoDetailsAction.OnSetPrimaryRecording -> setPrimaryRecording(action.recordingId)
            is DemoDetailsAction.OnTagClick -> _state.update { state ->
                state.copy(
                    tagOptions = state.tagOptions.map { tag ->
                        if (tag.name == action.tagModel.name) {
                            tag.copy(isSelected = !tag.isSelected)
                        } else {
                            tag
                        }
                    }
                )
            }

            DemoDetailsAction.OnTagCloseClick -> _state.update { it.copy(showTagSection = false) }

            DemoDetailsAction.OnTagIconClick -> _state.update { state ->
                state.copy(showTagSection = !state.showTagSection)
            }

            is DemoDetailsAction.OnTogglePlayback -> togglePlayback(action.recordingId)
            DemoDetailsAction.OnToggleRecording -> {
                if (_state.value.isRecording) stopRecording() else startRecording()
            }

            is DemoDetailsAction.OnTagOptionsLoaded -> {
                _state.update { state ->
                    state.copy(tagOptions = action.tags.toTagModels())
                }
            }
        }
    }

    private fun addCustomTag() {
        val trimmedName = _state.value.newTagTextState.text.toString().trim()
        if (trimmedName.isBlank()) return

        _state.update { state ->
            val alreadyExists = state.tagOptions.any { it.name.equals(trimmedName, ignoreCase = true) }
            state.copy(
                tagOptions = if (alreadyExists) {
                    state.tagOptions.map {
                        if (it.name.equals(trimmedName, ignoreCase = true)) it.copy(isSelected = true) else it
                    }
                } else {
                    state.tagOptions + TagModel(name = trimmedName, isSelected = true)
                },
                showAddTagSection = false
            )
        }
        _state.value.newTagTextState.clearText()
    }

    private fun saveChanged() {
        val currentState = _state.value
        if (currentState.isSaving) return

        val selectedColor = currentState.colorOptions.firstOrNull { it.isSelected }
            ?: currentState.colorOptions.first()

        val demo = Demo(
            id = currentState.demoId,
            title = currentState.titleTextState.text.toString().trim(),
            createdAtMillis = currentState.createdAtMillis,
            colorLabel = selectedColor.color.toArgb().toLong(),
            genres = currentState.tagOptions.filter { it.isSelected }.map { it.name },
            lyrics = currentState.lyricsTextState.text.toString().trim(),
            recordings = currentState.recordings.map { item ->
                Recording(
                    id = item.id,
                    title = item.titleState.text.toString().trim(),
                    durationMillis = item.durationSeconds * ONE_SECOND_MILLIS,
                    filePath = item.filePath,
                    isPrimary = item.isPrimary,
                    createdAtMillis = currentState.createdAtMillis
                )
            }
        )

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            runCatching { demoDetailsRepo.saveDemoDetailsChanged(demo) }
            _state.update { it.copy(isSaving = false) }
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
            val selectedGenreTags =
                demo.genres.map { genre -> TagModel(name = genre, isSelected = true) }
            val mergedTagOptions = state.tagOptions.map { tag ->
                tag.copy(isSelected = demo.genres.any { it.equals(tag.name, ignoreCase = true) })
            } + selectedGenreTags.filterNot { newTag ->
                state.tagOptions.any { it.name.equals(newTag.name, ignoreCase = true) }
            }

            state.copy(
                demoId = demo.id,
                createdAtMillis = demo.createdAtMillis,
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
        val file = audioRecorder.stop()

        _state.update { state ->
            state.copy(
                isRecording = false,
                recordingFilePath = file?.absolutePath ?: state.recordingFilePath
            )
        }
    }

    private fun addNewRecording() {
        val currentState = _state.value
        val filePath = currentState.recordingFilePath ?: return

        val labelText = currentState.newRecordingLabelState.text.toString().trim()
        val title = labelText.ifBlank {
            if (currentState.recordings.isEmpty()) {
                getApplication<Application>().getString(R.string.first_take)
            } else {
                getApplication<Application>().getString(
                    R.string.take_number,
                    currentState.recordings.size + 1
                )
            }
        }

        val newRecording = RecordingItemUi(
            id = UUID.randomUUID().toString(),
            titleState = TextFieldState(title),
            filePath = filePath,
            durationSeconds = currentState.recordingSeconds,
            isPrimary = currentState.recordings.isEmpty()
        )

        _state.update { state ->
            state.copy(
                recordings = state.recordings + newRecording,
                isAddingRecording = false,
                isRecording = false,
                recordingSeconds = 0,
                recordingFilePath = null,
                newRecordingLabelState = TextFieldState()
            )
        }
    }

    private fun closeNewRecordingSection() {
        timerJob?.cancel()
        timerJob = null
        if (_state.value.isRecording) {
            audioRecorder.stop()
        }
        _state.value.recordingFilePath?.let { File(it).delete() }

        _state.update { state ->
            state.copy(
                isAddingRecording = false,
                isRecording = false,
                recordingSeconds = 0,
                recordingFilePath = null,
                newRecordingLabelState = TextFieldState()
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
            updateRecordingItem(recordingId) {
                it.copy(
                    isPlaying = false,
                    currentDuration = positionSeconds(it.durationSeconds)
                )
            }
            return
        }

        val previousPlayingId = playingRecordingId
        if (previousPlayingId != null && previousPlayingId != recordingId) {
            updateRecordingItem(previousPlayingId) {
                it.copy(
                    isPlaying = false,
                    currentDuration = positionSeconds(it.durationSeconds)
                )
            }
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

    private fun updateRecordingItem(
        recordingId: String,
        transform: (RecordingItemUi) -> RecordingItemUi
    ) {
        _state.update { state ->
            state.copy(
                recordings = state.recordings.map { item ->
                    if (item.id == recordingId) transform(item) else item
                }
            )
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
