package com.mark.shereheke.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = RoyalGold,
    onPrimary = DeepSpace,
    primaryContainer = GoldVariant,
    onPrimaryContainer = Champagne,
    secondary = Champagne,
    onSecondary = DeepSpace,
    background = DeepSpace,
    onBackground = PureWhite,
    surface = ElevatedSurface,
    onSurface = PureWhite,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = MutedSlate,
    outline = SurfaceVariant
)

// We'll keep a clean light version, but the app defaults to luxury dark
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = RoyalGold,
    background = Color(0xFFF8F9FA),
    surface = Color.White
)

@Composable
fun SHEREHEKETheme(
    darkTheme: Boolean = true, // Force Dark Theme for the luxury experience
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
