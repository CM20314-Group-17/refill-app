package uk.ac.bath.cm20314.refill.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColourScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColourScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/** Displays the content using the app's theme. */
@Composable
fun RefillTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colours = if (darkTheme) DarkColourScheme else LightColourScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insets = WindowCompat.getInsetsController(window, view)

            // Change status bar colours.
            window.statusBarColor = colours.background.toArgb()
            insets.isAppearanceLightStatusBars = !darkTheme

            // Change navigation bar colours.
            // Only works on API level 26 or higher.
            if (Build.VERSION.SDK_INT >= 26) {
                window.navigationBarColor = colours.background.toArgb()
                insets.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colours,
        content = content,
        typography = Typography
    )
}