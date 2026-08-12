package com.song.demos.presentation.addnewdemo

sealed interface AddNewDemoEvent {
    data object DemoCreated : AddNewDemoEvent
    data class DemoCreationFailed(val message: String?) : AddNewDemoEvent
}
