package com.song.core.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)
}
