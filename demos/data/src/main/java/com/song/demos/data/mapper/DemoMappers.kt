package com.song.demos.data.mapper

import com.song.database.entity.DemoEntity
import com.song.database.entity.DemoWithRecordings
import com.song.database.entity.RecordingEntity
import com.song.demos.domain.repo.model.Demo
import com.song.demos.domain.repo.model.Recording

fun DemoWithRecordings.toDemo(): Demo = Demo(
    id = demo.id,
    title = demo.title,
    createdAtMillis = demo.createdAtMillis,
    colorLabel = demo.colorLabel,
    genres = demo.genres,
    lyrics = demo.lyrics,
    recordings = recordings.map { it.toRecording() }
)

fun RecordingEntity.toRecording(): Recording = Recording(
    id = id,
    title = title,
    durationMillis = durationMillis,
    filePath = filePath,
    isPrimary = isPrimary,
    createdAtMillis = createdAtMillis
)

fun Demo.toDemoEntity(): DemoEntity = DemoEntity(
    id = id,
    title = title,
    createdAtMillis = createdAtMillis,
    colorLabel = colorLabel,
    genres = genres,
    lyrics = lyrics
)

fun Demo.toRecordingEntities(): List<RecordingEntity> =
    recordings.map { it.toRecordingEntity(demoId = id) }

fun Recording.toRecordingEntity(demoId: String): RecordingEntity = RecordingEntity(
    id = id,
    demoId = demoId,
    title = title,
    durationMillis = durationMillis,
    filePath = filePath,
    isPrimary = isPrimary,
    createdAtMillis = createdAtMillis
)
