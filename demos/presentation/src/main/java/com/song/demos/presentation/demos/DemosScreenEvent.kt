package com.song.demos.presentation.demos

sealed interface DemosScreenEvent {
    data object AddDemoEvent: DemosScreenEvent
    data object OpenDemoDetailEvent: DemosScreenEvent
    data object OpenDeleteEvent: DemosScreenEvent
}