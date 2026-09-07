package com.song.demos.presentation.demos

import com.song.core.domain.settings.ThemeMode
import com.song.core.presentation.ui.util.AppLanguage
import com.song.demos.presentation.demos.model.DemoUi

data class DemosScreenState(
    val demos: List<DemoUi> = emptyList(),
    val demoCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSettingsSheet: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val showDeleteDialog: Boolean = false,
    val deletingDemoId: String = ""
)