package com.song.demos.presentation.demodetail

import com.song.demos.presentation.demos.model.ColorModel
import com.song.demos.presentation.demos.model.TagModel

sealed interface DemoDetailsAction {
    data object OnColorIconClick: DemoDetailsAction
    data class OnColorSelect(val color: ColorModel): DemoDetailsAction
    data object OnTagIconClick: DemoDetailsAction
    data class OnTagClick(val tagModel: TagModel): DemoDetailsAction
    data object OnTagCloseClick: DemoDetailsAction
    data object OnCustomTagClick : DemoDetailsAction
    data object OnAddCustomTagClick : DemoDetailsAction
    data object OnNewRecordingClick: DemoDetailsAction
    data class OnTogglePlayback(val recordingId: String): DemoDetailsAction
    data class OnSetPrimaryRecording(val recordingId: String): DemoDetailsAction
    data class OnDeleteRecording(val recordingId: String) : DemoDetailsAction
    data class OnAddNewRecording(val recordingId: String) : DemoDetailsAction
    data object OnCloseNewRecordingSection: DemoDetailsAction
    data object OnSaveDemoClick: DemoDetailsAction
    data object OnToggleRecording: DemoDetailsAction
}