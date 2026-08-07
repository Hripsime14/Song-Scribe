package com.song.demos.domain.repo.model

data class Demo(
    val id: String,
    val title: String,
    val createdAtMillis: Long,
    val colorLabel: Long,
    val genres: List<String>,
    val recordings: List<Recording>
)
