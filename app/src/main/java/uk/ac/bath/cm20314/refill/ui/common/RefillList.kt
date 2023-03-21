package uk.ac.bath.cm20314.refill.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 *  Displays items in a responsive list. Adjusts the number of columns based on the screen width.
 *
 *  @param items the items that the list displays.
 *  @param itemContent the content for each list item.
 */
@Composable
fun <T> RefillList(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    if (items.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 250.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                itemContent(item)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Empty",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge + TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

/**
 * A clickable card containing a title, label and preview image.
 *
 * @param title the title text in the card.
 * @param label the label text underneath the title.
 * @param onClick the function called when the user clicks the card.
 * @param preview the content displayed above the title, such as a preview image.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefillCard(
    title: String,
    label: String,
    onClick: () -> Unit,
    bubble: Boolean = false,
    preview: @Composable () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column {
            Box {
                preview()
                if (bubble) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}