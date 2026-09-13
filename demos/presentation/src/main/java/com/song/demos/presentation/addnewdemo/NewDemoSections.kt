package com.song.demos.presentation.addnewdemo

sealed interface NewDemoSections {

    data object Recording : NewDemoSections
    data object DemoTitle : NewDemoSections
    data object ColorLabel : NewDemoSections
    data object Tags : NewDemoSections
    data object Lyrics : NewDemoSections

    companion object {
        val default = listOf(Recording, DemoTitle, ColorLabel, Tags, Lyrics)
    }
}