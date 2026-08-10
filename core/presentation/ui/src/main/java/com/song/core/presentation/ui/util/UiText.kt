package com.song.core.presentation.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class DynamicText(val value: String): UiText
    class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicText -> value
            is StringResource -> stringResource(this.id, *args)
        }
    }


    fun asString(context: Context): String {
        return when(this) {
            is DynamicText -> value
            is StringResource -> context.getString(this.id, *args)
        }
    }
}