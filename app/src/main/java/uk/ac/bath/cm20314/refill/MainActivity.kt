package uk.ac.bath.cm20314.refill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import uk.ac.bath.cm20314.refill.ui.RefillApp

/** Provides a screen to display the app's user interface. */
class MainActivity : ComponentActivity() {

    /** Called when the system first creates the activity. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Allow content to appear behind the status bar and navigation bar.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Display content on the screen using 'composable' functions.
        // Each composable function calls other composable functions to display UI elements.
        setContent { RefillApp() }
    }
}
