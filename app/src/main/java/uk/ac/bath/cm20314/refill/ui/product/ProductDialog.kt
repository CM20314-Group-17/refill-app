package uk.ac.bath.cm20314.refill.ui.product

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.ui.common.DialogInput
import uk.ac.bath.cm20314.refill.ui.common.FullscreenDialog
import uk.ac.bath.cm20314.refill.ui.common.ThumbnailPicker

@Composable
fun ProductDialog(
    visible: Boolean,
    onClose: () -> Unit,
    onSave: (Product) -> Unit,
    product: Product,
    heading: @Composable () -> Unit,
) {
    var name by rememberSaveable(visible) {
        mutableStateOf(product.productName)
    }
    var price by rememberSaveable(visible) {
        mutableStateOf(product.pricePerKg.toString())
    }
    var portionSize by rememberSaveable(visible) {
        mutableStateOf(product.portionSize.toString())
    }
    var thumbnail by rememberSaveable(visible) {
        mutableStateOf(product.thumbnail)
    }

    FullscreenDialog(
        visible = visible,
        onClose = onClose,
        heading = heading,
        actions = {
            TextButton(
                onClick = {
                    onSave(
                        product.copy(
                            productName = name,
                            pricePerKg = price.toIntOrNull() ?: 0,
                            portionSize = portionSize.toFloatOrNull() ?: 0f,
                            thumbnail = thumbnail
                        )
                    )
                    onClose()
                }
            ) {
                Text(text = "Save")
            }
        }
    ) {
        DialogInput(
            label = stringResource(R.string.product_name),
            value = name,
            onValueChange = { name = it }
        )
        DialogInput(
            label = stringResource(R.string.product_price_label),
            value = price,
            onValueChange = { price = it },
            numeric = true
        )
        DialogInput(
            label = stringResource(R.string.product_portion_size),
            value = portionSize,
            onValueChange = { portionSize = it },
            numeric = true
        )
        ThumbnailPicker(
            thumbnail = thumbnail,
            onSelect = { thumbnail = it },
        )
    }
}