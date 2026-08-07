package com.song.database.converter

import androidx.room.TypeConverter

class StringListConverter {

    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(SEPARATOR)

    @TypeConverter
    fun toList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(SEPARATOR)

    companion object {
        private const val SEPARATOR = ","
    }
}
