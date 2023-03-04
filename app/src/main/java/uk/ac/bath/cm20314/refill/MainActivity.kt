package uk.ac.bath.cm20314.refill

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import uk.ac.bath.cm20314.refill.data.nfc.LocalNfc
import uk.ac.bath.cm20314.refill.data.nfc.NfcRepositoryImpl
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl
import uk.ac.bath.cm20314.refill.ui.RefillApp
import uk.ac.bath.cm20314.refill.ui.category.CategoryViewModel

/** Provides a screen to display the app's user interface. */
class MainActivity : ComponentActivity() {

    private lateinit var nfcRepository: NfcRepositoryImpl
    private lateinit var viewModel: CategoryViewModel

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

        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        callbackResponse()
    }

    private fun callbackResponse() {
        viewModel.callbackResponse(object: ProductRepositoryImpl.FirebaseCallback {
            override fun onResponse(response: ProductRepositoryImpl.Response) {
                print(response)
            }
        })
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

    private fun print(response: ProductRepositoryImpl.Response) {
        response.products?.let { products ->
            products.forEach { product ->
                product.name?.let {
                    Log.i(TAG, it)
                }
            }
        }

        response.exception?.let { exception ->
            exception.message?.let {
                Log.e(TAG, it)
            }
        }
    }
}