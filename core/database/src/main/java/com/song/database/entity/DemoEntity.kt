package com.song.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "demo")
data class DemoEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String
)