package com.song.core.data.settings

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.song.core.domain.settings.SettingsRepo
import com.song.core.domain.settings.ThemeMode
import kotlinx.coroutines.flow.map

class SettingsRepoImpl(
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : SettingsRepo {

    private val systemDefaultThemeMode: ThemeMode
        get() {
            val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return if (nightMode == Configuration.UI_MODE_NIGHT_YES) ThemeMode.DARK else ThemeMode.LIGHT
        }

    override val themeMode = dataStore.data.map { preferences ->
        val name = preferences[THEME_MODE_KEY]
        name?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() } ?: systemDefaultThemeMode
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.name
        }
    }

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }
}
