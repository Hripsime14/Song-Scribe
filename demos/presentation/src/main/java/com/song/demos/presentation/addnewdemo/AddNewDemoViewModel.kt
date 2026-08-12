package com.song.demos.presentation.addnewdemo

import android.app.Application
import androidx.compose.foundation.text.input.clearText
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.song.demos.domain.repo.NewDemoRepo
import com.song.demos.domain.repo.model.Demo
import com.song.demos.domain.repo.model.Recording
import com.song.demos.presentation.R
import com.song.demos.presentation.addnewdemo.recorder.AudioRecorder
import com.song.demos.presentation.demos.mapper.toTagModels
import com.song.demos.presentation.demos.model.TagModel
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

class AddNewDemoViewModel(
    application: Application,
    private val newDemoRepo: NewDemoRepo
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AddNewDemoState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<AddNewDemoEvent>()
    val events = eventChannel.receiveAsFlow()

    private val audioRecorder = AudioRecorder(application)
    private var timerJob: Job? = null

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

            AddNewDemoAction.OnDiscardRecording -> discardRecording()

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
        val file = audioRecorder.stop()
        _state.update { state ->
            state.copy(
                isRecording = false,
                recordingFilePath = file?.absolutePath ?: state.recordingFilePath
            )
        }
    }

    private fun discardRecording() {
        timerJob?.cancel()
        timerJob = null
        audioRecorder.stop()
        _state.value.recordingFilePath?.let { path -> File(path).delete() }
        _state.update { state ->
            state.copy(
                isRecording = false,
                recordingSeconds = 0,
                recordingFilePath = null
            )
        }
    }

    private fun createDemo() {
        val currentState = _state.value
        if (currentState.isSaving) return
        if (currentState.isRecording) stopRecording()

        val recordingPath = _state.value.recordingFilePath ?: return
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
            recordings = listOf(
                Recording(
                    id = UUID.randomUUID().toString(),
                    title = getApplication<Application>().getString(R.string.first_take),
                    durationMillis = _state.value.recordingSeconds * ONE_SECOND_MILLIS,
                    filePath = recordingPath,
                    isPrimary = true,
                    createdAtMillis = createdAtMillis
                )
            )
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
    }

    companion object {
        private const val ONE_SECOND_MILLIS = 1000L
    }
}
