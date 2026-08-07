package com.song.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.song.database.converter.StringListConverter
import com.song.database.dao.DemoDao
import com.song.database.entity.DemoEntity
import com.song.database.entity.RecordingEntity

@Database(
    entities = [
        DemoEntity::class,
        RecordingEntity::class
    ],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class DemoDatabase : RoomDatabase() {

    abstract fun demoDao(): DemoDao
}