package com.song.demos.presentation.addnewdemo

import com.song.demos.presentation.demos.model.ColorModel
import com.song.demos.presentation.demos.model.TagModel

sealed interface AddNewDemoAction {
    data class OnTagOptionsLoaded(val tags: List<String>) : AddNewDemoAction
    data class OnColorSelect(val color: ColorModel) : AddNewDemoAction
    data class OnTagClick(val tag: TagModel) : AddNewDemoAction
    data object OnToggleRecording : AddNewDemoAction
    data class OnDeleteRecording(val recordingId: String) : AddNewDemoAction
    data class OnTogglePlayback(val recordingId: String) : AddNewDemoAction
    data class OnSetPrimaryRecording(val recordingId: String) : AddNewDemoAction

    data object OnCustomTagClick : AddNewDemoAction
    data object OnAddCustomTagClick : AddNewDemoAction
    data object OnCreateDemoClick : AddNewDemoAction
}
