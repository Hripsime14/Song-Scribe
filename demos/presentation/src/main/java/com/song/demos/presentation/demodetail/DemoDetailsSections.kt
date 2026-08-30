package com.song.demos.presentation.demodetail

sealed interface DemoDetailsSections {
    data object DateAndGenre: DemoDetailsSections
    data object DemoTitle: DemoDetailsSections
    data object Recordings: DemoDetailsSections
    data object Lyrics: DemoDetailsSections
    data object Info: DemoDetailsSections

    companion object {
        val default = listOf(DateAndGenre, DemoTitle, Recordings, Lyrics, Info)
    }
}