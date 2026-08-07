package com.song.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "demo")
data class DemoEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val createdAtMillis: Long,
    val colorLabel: Long,
    val genres: List<String>
)