package com.song.core.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.song.core.data.settings.SettingsRepoImpl
import com.song.core.domain.settings.SettingsRepo
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

val coreDataModule = module {
    single { androidContext().settingsDataStore }
    singleOf(::SettingsRepoImpl) { bind<SettingsRepo>() }
}
