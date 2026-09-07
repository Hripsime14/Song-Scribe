package com.song.demos.presentation.demos

import com.song.core.domain.settings.ThemeMode
import com.song.core.presentation.ui.util.AppLanguage

sealed interface DemosScreenAction {
    data class onTogglePlayClick(val demoId: String, val isPlaying: Boolean): DemosScreenAction
    data class onDeleteRequest(val demoId: String): DemosScreenAction
    data object onDismissDeleteDialog: DemosScreenAction
    data object onAcceptDeleteDialog: DemosScreenAction
    data class onItemClick(val demoId: String): DemosScreenAction
    data object onAddDemoClick: DemosScreenAction
    data object onSettingsClick: DemosScreenAction
    data object onDismissSettingsSheet: DemosScreenAction
    data class onThemeModeSelected(val themeMode: ThemeMode): DemosScreenAction
    data class onLanguageSelected(val language: AppLanguage): DemosScreenAction
}