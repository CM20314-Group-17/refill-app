package uk.ac.bath.cm20314.refill

import android.content.Intent
import android.os.Bundle
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

        // Allow content to appear behind the status bar and navigation bar.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        nfcRepository = NfcRepositoryImpl(this, ProductRepositoryImpl)

        // Display content on the screen using 'composable' functions.
        // Each composable function calls other composable functions to display UI elements.
        setContent {
            CompositionLocalProvider(LocalNfc provides nfcRepository) {
                RefillApp()
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.action

        // TODO: Only call NFC repository when the intent is NFC-related.
        nfcRepository.onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        nfcRepository.enable()
    }

    override fun onPause() {
        super.onPause()
        nfcRepository.disable()
    }
}