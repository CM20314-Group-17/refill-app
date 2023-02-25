package uk.ac.bath.cm20314.refill.data.nfc

import android.content.Context
import android.content.Intent
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository

class NfcRepositoryImpl(
    private val activity: Context,
    private val repository: ProductRepository
) : NfcRepository {

    fun enable() {
        // TODO
    }

    fun disable() {
        // TODO
    }

    fun onNewIntent(intent: Intent?) {
        // TODO
    }

    override suspend fun writeProductInformation(product: Product): Boolean {
        // TODO: Also make sure to update the isUpdated field using the ProductRepository.
        return false
    }
}