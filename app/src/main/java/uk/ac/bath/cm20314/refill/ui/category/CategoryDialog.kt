package uk.ac.bath.cm20314.refill.ui.category

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.ui.common.DialogInput
import uk.ac.bath.cm20314.refill.ui.common.FullscreenDialog
import uk.ac.bath.cm20314.refill.ui.common.ThumbnailPicker

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
        DialogInput(
            label = stringResource(R.string.category_name),
            value = name,
            onValueChange = { name = it }
        )
        ThumbnailPicker(
            thumbnail = thumbnail,
            onSelect = { thumbnail = it },
        )
    }
}