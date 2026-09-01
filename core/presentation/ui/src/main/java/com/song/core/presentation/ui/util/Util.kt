package com.song.core.presentation.ui.util

fun countWords(text: String): Int {
    return text
        .trim()
        .split(Regex("\\s+"))
        .count { it.isNotEmpty() }
}