package com.song.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.song.database.entity.DemoEntity

@Database(
    entities = [
        DemoEntity::class
               ],
    version = 1
)
abstract class DemoDatabase: RoomDatabase() {

}