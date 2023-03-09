package uk.ac.bath.cm20314.refill.data.nfc

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.Channel
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository

class NfcRepositoryImpl(
    private val activity: Activity,
    private val repository: ProductRepository
) : NfcRepository, DefaultLifecycleObserver {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)
    private val messages = Channel<String>()

    /** Starts listening for nearby NFC tags. */
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        nfcAdapter?.enableReaderMode(
            activity,
            this::onTagDiscovered,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NFC_BARCODE,
            null
        )
    }

    /** Stops listening for nearby NFC tags. */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        nfcAdapter?.disableReaderMode(activity)
    }

    /** Called when the device discovers an NFC tag nearby. */
    private fun onTagDiscovered(tag: Tag) {
        val payload = messages.tryReceive().getOrNull() ?: return
        val message = NdefMessage(NdefRecord.createTextRecord("en", payload))

        Ndef.get(tag)?.use { ndef ->
            ndef.connect()
            ndef.writeNdefMessage(message)
        } ?: NdefFormatable.get(tag)?.use { format ->
            format.connect()
            format.format(message)
        }

        activity.runOnUiThread {
            Toast.makeText(activity, "NFC tag written", Toast.LENGTH_LONG).show()
        }
    }

    override fun isNfcSupported() = nfcAdapter != null

    override suspend fun writeProductInformation(product: Product) {
        if (!isNfcSupported()) {
            Toast.makeText(activity, "NFC is not enabled", Toast.LENGTH_LONG).show()
        }

        val nameCode = product.productName.padEnd(length = 12).subSequence(0, 12)
        messages.send(element = "${product.portionSize}_${product.pricePerKg}_${nameCode}_")
        repository.updateProduct(product.copy(isUpdated = true))
    }
}