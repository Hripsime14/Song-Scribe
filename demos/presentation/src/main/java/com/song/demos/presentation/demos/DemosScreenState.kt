package com.song.demos.presentation.demos

import com.song.demos.presentation.demos.model.DemoUi

data class DemosScreenState(
    val demos: List<DemoUi> = emptyList(),
    val demoCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val deletingDemoId: String = ""
)