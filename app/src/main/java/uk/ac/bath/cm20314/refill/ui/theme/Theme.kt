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

private val LightColourScheme = lightColorScheme(
    primary = DarkGreen,
    secondary = Salmon,
    tertiary = GreyGreen,
    background = LightGrey,
)

private val DarkColourScheme = darkColorScheme(
    primary = GreyGreen,
    secondary = Salmon,
    tertiary = LightGrey,
    background = DarkGreen,
)

/** Displays the content using the app's theme. */
@Composable
fun RefillTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current

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
        colorScheme = if (darkTheme) DarkColourScheme else LightColourScheme,
        typography = Typography,
    )
}