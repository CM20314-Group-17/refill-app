package uk.ac.bath.cm20314.refill.data.nfc

import androidx.compose.runtime.staticCompositionLocalOf
import uk.ac.bath.cm20314.refill.data.product.Product

val LocalNfc = staticCompositionLocalOf<NfcRepository> { error("No NFC repository") }

interface NfcRepository {

    /** Checks whether the device supports NFC. */
    fun isNfcSupported(): Boolean

    /** Writes product information to the next NFC tag tapped on the device. */
    suspend fun writeProductInformation(product: Product)
}