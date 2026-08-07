 package com.song.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recording",
    foreignKeys = [
        ForeignKey(
            entity = DemoEntity::class,
            parentColumns = ["id"],
            childColumns = ["demoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("demoId")]
)
data class RecordingEntity(
    @PrimaryKey
    val id: String,
    val demoId: String,
    val title: String,
    val durationMillis: Long,
    val filePath: String,
    val isPrimary: Boolean,
    val createdAtMillis: Long
)
