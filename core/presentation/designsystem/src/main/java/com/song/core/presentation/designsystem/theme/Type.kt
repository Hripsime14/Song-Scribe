package com.song.core.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val SongScribeFontFamily = FontFamily.Default

val SongScribeTypography = Typography(

    // Large timer, for example "0:00" on the recording screen.
    displaySmall = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    // Reserved for unusually large screen content.
    headlineLarge = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    // Main screen titles: "My Demos", "New Demo", "Demo Details".
    headlineMedium = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Smaller prominent headings.
    headlineSmall = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Main content title, such as the demo title on the detail screen.
    titleLarge = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Recording names and card titles.
    titleMedium = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    // Smaller card titles and emphasized row labels.
    titleSmall = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Input text and other important body text.
    bodyLarge = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Standard subtitles and descriptions.
    bodyMedium = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // Dates, durations and secondary metadata.
    bodySmall = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    ),

    // Button text, for example "Save", "Create" and "Add".
    labelLarge = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Section labels and tags.
    labelMedium = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp
    ),

    // Uppercase field labels, metadata and small badges.
    labelSmall = TextStyle(
        fontFamily = SongScribeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.6.sp
    )
)