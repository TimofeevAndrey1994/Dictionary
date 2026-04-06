package com.timofeev.words.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,

    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,

    primary = LightPrimary,
    onPrimary = LightOnPrimary
)

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,

    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,

    primary = DarkPrimary,
    onPrimary = DarkOnPrimary
)

@Composable
fun WordsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WordsTypography,
        content = content
    )
}
