package com.song.core.presentation.designsystem.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,

    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,

    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    inversePrimary = DarkInversePrimary,

    scrim = DarkScrim,
    surfaceTint = DarkSurfaceTint
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,

    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,

    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    inversePrimary = LightInversePrimary,

    scrim = LightScrim,
    surfaceTint = LightSurfaceTint
)

@Composable
fun SongScribeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = animateColorScheme(colorScheme),
        typography = SongScribeTypography,
        content = content
    )
}

@Composable
private fun animateColorScheme(target: ColorScheme): ColorScheme {
    val spec = tween<Color>(durationMillis = 350)

    @Composable
    fun Color.animated() = animateColorAsState(this, spec).value

    return target.copy(
        primary = target.primary.animated(),
        onPrimary = target.onPrimary.animated(),
        primaryContainer = target.primaryContainer.animated(),
        onPrimaryContainer = target.onPrimaryContainer.animated(),
        inversePrimary = target.inversePrimary.animated(),
        secondary = target.secondary.animated(),
        onSecondary = target.onSecondary.animated(),
        secondaryContainer = target.secondaryContainer.animated(),
        onSecondaryContainer = target.onSecondaryContainer.animated(),
        tertiary = target.tertiary.animated(),
        onTertiary = target.onTertiary.animated(),
        tertiaryContainer = target.tertiaryContainer.animated(),
        onTertiaryContainer = target.onTertiaryContainer.animated(),
        background = target.background.animated(),
        onBackground = target.onBackground.animated(),
        surface = target.surface.animated(),
        onSurface = target.onSurface.animated(),
        surfaceVariant = target.surfaceVariant.animated(),
        onSurfaceVariant = target.onSurfaceVariant.animated(),
        surfaceTint = target.surfaceTint.animated(),
        inverseSurface = target.inverseSurface.animated(),
        inverseOnSurface = target.inverseOnSurface.animated(),
        error = target.error.animated(),
        onError = target.onError.animated(),
        errorContainer = target.errorContainer.animated(),
        onErrorContainer = target.onErrorContainer.animated(),
        outline = target.outline.animated(),
        outlineVariant = target.outlineVariant.animated(),
        scrim = target.scrim.animated(),
        surfaceBright = target.surfaceBright.animated(),
        surfaceDim = target.surfaceDim.animated(),
        surfaceContainer = target.surfaceContainer.animated(),
        surfaceContainerHigh = target.surfaceContainerHigh.animated(),
        surfaceContainerHighest = target.surfaceContainerHighest.animated(),
        surfaceContainerLow = target.surfaceContainerLow.animated(),
        surfaceContainerLowest = target.surfaceContainerLowest.animated(),
        primaryFixed = target.primaryFixed.animated(),
        primaryFixedDim = target.primaryFixedDim.animated(),
        onPrimaryFixed = target.onPrimaryFixed.animated(),
        onPrimaryFixedVariant = target.onPrimaryFixedVariant.animated(),
        secondaryFixed = target.secondaryFixed.animated(),
        secondaryFixedDim = target.secondaryFixedDim.animated(),
        onSecondaryFixed = target.onSecondaryFixed.animated(),
        onSecondaryFixedVariant = target.onSecondaryFixedVariant.animated(),
        tertiaryFixed = target.tertiaryFixed.animated(),
        tertiaryFixedDim = target.tertiaryFixedDim.animated(),
        onTertiaryFixed = target.onTertiaryFixed.animated(),
        onTertiaryFixedVariant = target.onTertiaryFixedVariant.animated(),
    )
}