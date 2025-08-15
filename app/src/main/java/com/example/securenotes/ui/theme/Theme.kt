package com.example.securenotes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Immutable
data class ThemePrefs(
    val darkMode: Boolean,
    val fontScale: Float
)

fun scaledTypography(base: Typography, scale: Float): Typography {
    fun TextStyle.scale() = copy(fontSize = (fontSize.value * scale).sp)
    return Typography(
        displayLarge = base.displayLarge.scale(),
        displayMedium = base.displayMedium.scale(),
        displaySmall = base.displaySmall.scale(),
        headlineLarge = base.headlineLarge.scale(),
        headlineMedium = base.headlineMedium.scale(),
        headlineSmall = base.headlineSmall.scale(),
        titleLarge = base.titleLarge.scale(),
        titleMedium = base.titleMedium.scale(),
        titleSmall = base.titleSmall.scale(),
        bodyLarge = base.bodyLarge.scale(),
        bodyMedium = base.bodyMedium.scale(),
        bodySmall = base.bodySmall.scale(),
        labelLarge = base.labelLarge.scale(),
        labelMedium = base.labelMedium.scale(),
        labelSmall = base.labelSmall.scale(),
    )
}

@Composable
fun SecureNotesTheme(
    darkTheme: Boolean = false,
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = scaledTypography(Typography(), fontScale),
        content = content
    )
}
