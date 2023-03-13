package uk.ac.bath.cm20314.refill.ui.category

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.ui.common.FullscreenDialog
import uk.ac.bath.cm20314.refill.ui.common.ThumbnailPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    visible: Boolean,
    onClose: () -> Unit,
    onSave: (Category) -> Unit,
    heading: @Composable () -> Unit,
    category: Category,
) {
    var name by rememberSaveable(visible) {
        mutableStateOf(category.categoryName)
    }
    var thumbnail by rememberSaveable(visible) {
        mutableStateOf(category.thumbnail)
    }

    FullscreenDialog(
        visible = visible,
        onClose = onClose,
        heading = heading,
        actions = {
            TextButton(
                onClick = {
                    onSave(
                        category.copy(
                            categoryName = name,
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