package com.song.core.presentation.ui.util

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.addDefaultStartPadding(): Modifier {
    return this.padding(start = 8.dp)
}

fun Modifier.addDefaultTopPadding(): Modifier {
    return this.padding(top = 8.dp)
}