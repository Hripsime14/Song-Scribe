package com.song.demos.data.di

import com.song.database.DemoDatabase
import com.song.database.DemoDatabaseFactory
import com.song.demos.data.repo.DemoDetailsRepoImpl
import com.song.demos.data.repo.DemosRepoImpl
import com.song.demos.data.repo.NewDemoRepoImpl
import com.song.demos.domain.repo.DemoDetailsRepo
import com.song.demos.domain.repo.DemosRepo
import com.song.demos.domain.repo.NewDemoRepo
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val demosDataModule = module {
    single { DemoDatabaseFactory.create(androidContext()) }
    single { get<DemoDatabase>().demoDao() }

    singleOf(::DemosRepoImpl) { bind<DemosRepo>() }
    singleOf(::NewDemoRepoImpl) { bind<NewDemoRepo>() }
    singleOf(::DemoDetailsRepoImpl) { bind<DemoDetailsRepo>() }
}
