package com.song.scribe

import android.app.Application
import com.song.demos.data.di.demosDataModule
import com.song.demos.presentation.di.demosPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SongScribeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SongScribeApp)
            modules(
                demosDataModule,
                demosPresentationModule
            )
        }
    }
}
