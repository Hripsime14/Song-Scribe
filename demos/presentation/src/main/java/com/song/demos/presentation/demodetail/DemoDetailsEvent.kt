package com.song.demos.presentation.demodetail

sealed interface DemoDetailsEvent {
    data object DemoSaved : DemoDetailsEvent
    data class DemoSaveFailed(val message: String?) : DemoDetailsEvent
}
