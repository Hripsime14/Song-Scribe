package com.song.demos.presentation.demodetail

sealed interface DemoDetailsSections {
    data object ColorLabel: DemoDetailsSections
    data object Tags: DemoDetailsSections
    data object DemoTitle: DemoDetailsSections
    data object Recordings: DemoDetailsSections
    data object Lyrics: DemoDetailsSections
    data object Info: DemoDetailsSections

    companion object {
        val default = listOf(DemoTitle, ColorLabel, Tags, Recordings, Lyrics, Info)
    }
}