package com.song.demos.presentation.common

import androidx.compose.ui.graphics.toArgb
import com.song.demos.presentation.addnewdemo.model.RecordingItemUi
import com.song.demos.presentation.demos.model.ColorModel
import com.song.demos.presentation.demos.model.TagModel

data class DemoSnapshot(
    val title: String = "",
    val lyrics: String = "",
    val colorArgb: Long? = null,
    val genres: Set<String> = emptySet(),
    val recordingIds: List<String> = emptyList(),
    val recordingTitles: List<String> = emptyList(),
    val primaryRecordingId: String? = null
)

fun buildDemoSnapshot(
    title: String,
    lyrics: String,
    colorOptions: List<ColorModel>,
    tagOptions: List<TagModel>,
    recordings: List<RecordingItemUi>
): DemoSnapshot {
    return DemoSnapshot(
        title = title,
        lyrics = lyrics,
        colorArgb = colorOptions.firstOrNull { it.isSelected }?.color?.toArgb()?.toLong(),
        genres = tagOptions.filter { it.isSelected }.map { it.name }.toSet(),
        recordingIds = recordings.map { it.id },
        recordingTitles = recordings.map { it.titleState.text.toString() },
        primaryRecordingId = recordings.firstOrNull { it.isPrimary }?.id
    )
}
