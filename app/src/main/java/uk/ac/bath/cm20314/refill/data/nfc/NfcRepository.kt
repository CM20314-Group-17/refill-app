package uk.ac.bath.cm20314.refill.data.nfc

import androidx.compose.runtime.staticCompositionLocalOf
import uk.ac.bath.cm20314.refill.data.product.Product

val LocalNfc = staticCompositionLocalOf<NfcRepository> { error("No NFC repository") }

interface NfcRepository {

    /** Writes product information to the NFC tag and returns whether it was successful. */
    suspend fun writeProductInformation(product: Product): Boolean
}