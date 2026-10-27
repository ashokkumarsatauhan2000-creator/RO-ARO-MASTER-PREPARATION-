package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = Color.White,
    primaryContainer = NavyBlue,
    onPrimaryContainer = Color.White,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = EmeraldGreenSoft,
    tertiary = AmberGold,
    onTertiary = Color.Black,
    error = RoseRed,
    onError = Color.White,
    background = SlateBackgroundDark,
    onBackground = SlateTextPrimaryDark,
    surface = SurfaceDark,
    onSurface = SlateTextPrimaryDark,
    surfaceVariant = SlateCardDark,
    onSurfaceVariant = SlateTextSecondaryDark,
    outline = SlateBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = Color.White,
    primaryContainer = IndigoAccent,
    onPrimaryContainer = Color.White,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    secondaryContainer = EmeraldGreenSoft,
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = AmberGold,
    onTertiary = Color.Black,
    error = RoseRed,
    onError = Color.White,
    background = SlateBackgroundLight,
    onBackground = SlateTextPrimaryLight,
    surface = SurfaceWhite,
    onSurface = SlateTextPrimaryLight,
    surfaceVariant = SlateCardLight,
    onSurfaceVariant = SlateTextSecondaryLight,
    outline = SlateBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
