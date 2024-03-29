package uk.ac.bath.cm20314.refill

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import uk.ac.bath.cm20314.refill.data.nfc.LocalNfc
import uk.ac.bath.cm20314.refill.data.nfc.NfcRepositoryImpl
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl
import uk.ac.bath.cm20314.refill.ui.RefillApp

/** Provides a screen to display the app's user interface. */
class MainActivity : ComponentActivity() {

    private lateinit var nfcRepository: NfcRepositoryImpl

    /** Called when the system first creates the activity. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcRepository = NfcRepositoryImpl(this, ProductRepositoryImpl)
        lifecycle.addObserver(nfcRepository)

        // Allow content to appear behind the status bar and navigation bar.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Display content on the screen using 'composable' functions.
        // Each composable function calls other composable functions to display UI elements.
        setContent {
            CompositionLocalProvider(LocalNfc provides nfcRepository) {
                RefillApp()
            }
        }
    }
}