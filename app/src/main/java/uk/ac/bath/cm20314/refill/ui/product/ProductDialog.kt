package uk.ac.bath.cm20314.refill.ui.product

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.ui.common.FullscreenDialog
import uk.ac.bath.cm20314.refill.ui.common.ThumbnailPicker

@OptIn(ExperimentalMaterial3Api::class)
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
                            pricePerKg = price.toIntOrNull() ?: Product.Blank.pricePerKg,
                            portionSize = portionSize.toFloatOrNull() ?: Product.Blank.portionSize,
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
        Text(
            text = "Name",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            singleLine = true
        )
        Text(
            text = "Price",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = price,
            onValueChange = { price = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        )
        Text(
            text = "Portion size",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = portionSize,
            onValueChange = { portionSize = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        )
        Text(
            text = "Image",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        ThumbnailPicker(
            thumbnail = thumbnail,
            onSelect = { thumbnail = it },
        )
    }
}