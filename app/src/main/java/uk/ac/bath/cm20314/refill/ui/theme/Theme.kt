package uk.ac.bath.cm20314.refill.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColours = lightColorScheme(
    primary = Green20,
    onPrimary = Neutral90,
    primaryContainer = Green80,
    onPrimaryContainer = Green20,
    background = Neutral90,
    onBackground = Neutral10,
    surface = Neutral90,
    onSurface = Neutral10,
    surfaceVariant = Neutral80,
    onSurfaceVariant = Neutral50,
    outlineVariant = Neutral70,
    tertiary = Salmon30,
    onTertiary = Neutral90,
)

private val DarkColours = darkColorScheme(
    primary = Green60,
    onPrimary = Neutral10,
    primaryContainer = Green40,
    onPrimaryContainer = Neutral90,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral10,
    onSurface = Neutral90,
    surfaceVariant = Neutral20,
    onSurfaceVariant = Neutral80,
    outlineVariant = Neutral50,
    tertiary = Salmon60,
    onTertiary = Neutral10,
)

/** Displays the content using the app's theme. */
@Composable
fun RefillTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val colours = if (darkTheme) DarkColours else LightColours

    SideEffect {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            val insets = WindowCompat.getInsetsController(window, view)

            insets.isAppearanceLightStatusBars = !darkTheme
            insets.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        content = content,
        colorScheme = colours,
        typography = Typography,
    )
}