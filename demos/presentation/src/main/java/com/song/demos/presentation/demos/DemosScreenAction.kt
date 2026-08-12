package com.song.demos.presentation.demos

sealed interface DemosScreenAction {
    data class onTogglePlayClick(val demoId: String, val isPlaying: Boolean): DemosScreenAction
    data class onDeleteRequest(val demoId: String): DemosScreenAction
    data object onDismissDeleteDialog: DemosScreenAction
    data object onAcceptDeleteDialog: DemosScreenAction
    data class onItemClick(val demoId: String): DemosScreenAction
    data object onAddDemoClick: DemosScreenAction
}