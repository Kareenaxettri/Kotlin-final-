package com.example.dipindonuts.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DonutPink,
    onPrimary = Color.White,
    primaryContainer = DonutPink.copy(alpha = 0.1f),
    onPrimaryContainer = DonutPink,
    secondary = DonutPurple,
    onSecondary = Color.White,
    secondaryContainer = DonutPurple.copy(alpha = 0.1f),
    onSecondaryContainer = DonutPurple,
    tertiary = DonutOrange,
    onTertiary = Color.White,
    tertiaryContainer = DonutOrange.copy(alpha = 0.1f),
    onTertiaryContainer = DonutOrange,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = DonutCream,
    onSurfaceVariant = TextSecondaryLight,
    outline = DonutBrown.copy(alpha = 0.2f),
    outlineVariant = DonutBrown.copy(alpha = 0.1f),
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

private val DarkColorScheme = darkColorScheme(
    primary = DonutPink,
    onPrimary = Color.White,
    primaryContainer = DonutPink.copy(alpha = 0.2f),
    onPrimaryContainer = DonutPink,
    secondary = DonutPurple,
    onSecondary = Color.White,
    secondaryContainer = DonutPurple.copy(alpha = 0.2f),
    onSecondaryContainer = DonutPurple,
    tertiary = DonutOrange,
    onTertiary = Color.White,
    tertiaryContainer = DonutOrange.copy(alpha = 0.2f),
    onTertiaryContainer = DonutOrange,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = DonutChocolate.copy(alpha = 0.3f),
    onSurfaceVariant = TextSecondaryDark,
    outline = DonutCream.copy(alpha = 0.2f),
    outlineVariant = DonutCream.copy(alpha = 0.1f),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC)
)

@Composable
fun DipInDonutsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}