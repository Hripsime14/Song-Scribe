package com.song.demos.presentation.demos

sealed interface DemosScreenAction {
    data class onTogglePlayClick(val demoId: String, val isPlaying: Boolean): DemosScreenAction
    data class onMoreClick(val demoId: String): DemosScreenAction
    data class onItemClick(val demoId: String): DemosScreenAction
    data object onAddDemoClick: DemosScreenAction
}