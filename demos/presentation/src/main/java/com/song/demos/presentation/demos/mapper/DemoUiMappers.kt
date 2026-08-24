package com.song.demos.presentation.demos.mapper

import androidx.compose.ui.graphics.Color
import com.song.demos.domain.repo.model.Demo
import com.song.demos.domain.repo.model.Recording
import com.song.demos.presentation.demos.model.DemoUi
import com.song.demos.presentation.demos.model.RecordingUi
import com.song.demos.presentation.demos.model.TagModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun Demo.toDemoModel(): DemoUi = DemoUi(
    id = id,
    title = title,
    date = Instant
        .ofEpochMilli(createdAtMillis)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter),
    colorLabel = Color(colorLabel.toInt()),
    recording = recordings.firstOrNull { it.isPrimary }?.toRecordingItem(),
    genres = genres,
    moreRecordingCount = (recordings.size - 1).coerceAtLeast(0)
)

fun Recording.toRecordingItem(): RecordingUi = RecordingUi(
    id = id,
    title = title,
    duration = (durationMillis / 1000).toInt(),
    currentDuration = 0,
    isPrimary = isPrimary,
    recording = filePath,
    isPlaying = false
)

fun List<String>.toTagModels(): List<TagModel> = map ( String::toTagModel )

fun String.toTagModel(): TagModel = TagModel(
    name = this,
    isSelected = false
)