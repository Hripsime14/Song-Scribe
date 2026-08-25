package com.song.demos.presentation.di

import com.song.demos.presentation.addnewdemo.AddNewDemoViewModel
import com.song.demos.presentation.demos.DemosScreenViewModel
import com.song.demos.presentation.demos.player.DemoPlayer
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val demosPresentationModule = module {
    viewModel { AddNewDemoViewModel(androidApplication(), get(), get()) }
    singleOf(::DemoPlayer)
    viewModelOf(::DemosScreenViewModel)
}
