package uk.ac.bath.cm20314.refill.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.nfc.LocalNfc
import uk.ac.bath.cm20314.refill.ui.RefillLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    productId: String,
    viewModel: ProductViewModel = viewModel(factory = ProductViewModel.Factory(productId))
) {
    //
    // Temporary UI for testing NFC.
    //

    // Don't reuse the NFC coroutine scope since it may be cancelled.
    val nfcCoroutineScope = rememberCoroutineScope()
    val nfcRepository = LocalNfc.current
    val product by viewModel.product.collectAsState()
    var nfcDialogOpen by rememberSaveable { mutableStateOf(false) }

    RefillLayout(
        topBar = { /*TODO*/ }
    ) {
        Column {
            Text(text = product?.name ?: "")
            Button(
                onClick = {
                    nfcDialogOpen = true
                    product?.let { product ->
                        nfcCoroutineScope.launch {
                            nfcRepository.writeProductInformation(product)
                            nfcDialogOpen = false
                        }
                    }
                }
            ) {
                Text(text = "write")
            }
        }
    }

    if (nfcDialogOpen) {
        NfcDialog(
            onDismissRequest = {
                nfcCoroutineScope.coroutineContext.cancelChildren()
                nfcDialogOpen = false
            }
        )
    }
}

@Composable
private fun NfcDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        confirmButton = { /* No confirm button. */ },
        title = { Text(text = "Searching for NFC tag") },
        text = { Text(text = "Tap your device against the NFC tag.") },
        icon = { CircularProgressIndicator() }
    )
}