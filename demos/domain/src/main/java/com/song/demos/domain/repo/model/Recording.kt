package com.song.demos.domain.repo.model

data class Recording(
    val id: String,
    val title: String,
    val durationMillis: Long,
    val filePath: String,
    val isPrimary: Boolean,
    val createdAtMillis: Long
)
