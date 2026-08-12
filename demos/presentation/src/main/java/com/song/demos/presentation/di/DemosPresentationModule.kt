package com.song.demos.presentation.di

import com.song.demos.presentation.addnewdemo.AddNewDemoViewModel
import com.song.demos.presentation.demos.DemosScreenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val demosPresentationModule = module {
    viewModel { AddNewDemoViewModel(androidApplication(), get()) }
    viewModelOf(::DemosScreenViewModel)
}
