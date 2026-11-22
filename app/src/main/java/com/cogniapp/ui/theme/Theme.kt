package com.cogniapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightQuizColorScheme = lightColorScheme(
    primary = SkyBlue,
    onPrimary = CardWhite,
    primaryContainer = SkyTint,
    onPrimaryContainer = SkyText,
    secondary = DeepTeal,
    onSecondary = CardWhite,
    secondaryContainer = SoftMintReveal,
    onSecondaryContainer = SlateInkDark,
    tertiary = CoralOrange,
    onTertiary = CardWhite,
    background = WarmCanvas,
    onBackground = SlateInkDark,
    surface = CardWhite,
    onSurface = SlateInkDark,
    surfaceVariant = SoftMintReveal,
    onSurfaceVariant = SlateInkSecondary,
    outline = WarmBorder
)

private val DarkQuizColorScheme = darkColorScheme(
    primary = DarkSkyAction,
    onPrimary = CardWhite,
    primaryContainer = DarkSkyTint,
    onPrimaryContainer = DarkSkyText,
    secondary = DeepTeal,
    onSecondary = CardWhite,
    secondaryContainer = DarkSageReveal,
    onSecondaryContainer = DarkInkPrimary,
    tertiary = CoralOrange,
    onTertiary = CardWhite,
    background = CharcoalSlateCanvas,
    onBackground = DarkInkPrimary,
    surface = SlateCardElevated,
    onSurface = DarkInkPrimary,
    surfaceVariant = DarkSageReveal,
    onSurfaceVariant = DarkInkSecondary,
    outline = SlateCardBorder
)

/**
 * Access the CogniApp custom accessible colors and typography.
 */
object CogniTheme {
    val colors: CogniColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCogniColors.current

    val typography: androidx.compose.material3.Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

@Composable
fun CogniAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val cogniColors = if (darkTheme) darkCogniColors() else lightCogniColors()
    val colorScheme = if (darkTheme) DarkQuizColorScheme else LightQuizColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val barColor = if (darkTheme) CharcoalSlateCanvas.toArgb() else WarmCanvas.toArgb()
                window.statusBarColor = barColor
                window.navigationBarColor = barColor
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalCogniColors provides cogniColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CogniTypography,
            content = content
        )
    }
}
