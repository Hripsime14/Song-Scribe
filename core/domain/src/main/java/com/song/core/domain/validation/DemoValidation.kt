package com.song.core.domain.validation

object DemoValidationRules {
    const val TITLE_MAX_LENGTH = 30
    const val TAG_MAX_LENGTH = 30
    const val LYRICS_MAX_LENGTH = 5000
    const val MAX_RECORDINGS = 10
    const val MIN_RECORDING_SECONDS = 1
    const val MAX_RECORDING_SECONDS = 600
}

object DemoValidator {
    fun isTitleValid(title: String): Boolean =
        title.isNotBlank() && title.length <= DemoValidationRules.TITLE_MAX_LENGTH

    fun canAddRecording(currentCount: Int): Boolean =
        currentCount < DemoValidationRules.MAX_RECORDINGS

    fun isRecordingDurationValid(seconds: Int): Boolean =
        seconds >= DemoValidationRules.MIN_RECORDING_SECONDS

    fun canSaveDemo(
        title: String,
        recordingCount: Int,
        isSaving: Boolean,
        isRecording: Boolean,
        isAddingRecording: Boolean = false
    ): Boolean = !isSaving && !isRecording && !isAddingRecording &&
        isTitleValid(title) && recordingCount > 0
}
