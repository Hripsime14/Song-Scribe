package com.song.core.presentation.ui.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LocaleUtil {

    fun setAppLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.languageTag)
        )
    }

    fun getCurrentAppLanguage(): AppLanguage {
        val currentTag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        return AppLanguage.entries.firstOrNull { currentTag.startsWith(it.languageTag) } ?: AppLanguage.ENGLISH
    }
}
