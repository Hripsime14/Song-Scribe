package com.song.demos.presentation.demos

sealed interface DemosScreenEvent {
    data object AddDemoEvent: DemosScreenEvent
    data class OpenDemoDetailEvent(val demoId: String): DemosScreenEvent
    data object OpenDeleteEvent: DemosScreenEvent
}