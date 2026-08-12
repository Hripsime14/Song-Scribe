package com.song.database

import android.content.Context
import androidx.room.Room

object DemoDatabaseFactory {

    private const val DATABASE_NAME = "demo.db"

    fun create(context: Context): DemoDatabase {
        return Room
            .databaseBuilder(
                context.applicationContext,
                DemoDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
