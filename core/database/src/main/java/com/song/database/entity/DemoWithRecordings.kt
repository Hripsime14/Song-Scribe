package com.song.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DemoWithRecordings(
    @Embedded
    val demo: DemoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "demoId"
    )
    val recordings: List<RecordingEntity>
)
