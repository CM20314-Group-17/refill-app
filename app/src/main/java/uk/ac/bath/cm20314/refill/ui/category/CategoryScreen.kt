package uk.ac.bath.cm20314.refill.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.common.RefillCard
import uk.ac.bath.cm20314.refill.ui.common.RefillList

/** Displays a list of products within a category. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryId: String,
    navigateToProduct: (productId: String) -> Unit,
    viewModel: CategoryViewModel = viewModel(factory = CategoryViewModel.Factory(categoryId))
) {
    var editDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    val products by viewModel.products.collectAsState()
    val category by viewModel.category.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                CategoryViewModel.Event.CategoryUpdated -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Category renamed",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.category.collect()
                    }
                }
            }
        }
    }

    RefillLayout(
        topBar = { scrollBehaviour ->
            CategoryTopBar(
                categoryName = category?.name ?: "",
                editCategory = { editDialogOpen = true },
                scrollBehaviour = scrollBehaviour
            )
        },
        actions = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.category_add)
                )
            }
        },
        snackbarHostState = snackbarHostState
    ) {
        RefillList(items = products) { product ->
            RefillCard(
                title = product.name,
                label = "${product.pricePerKg}p / 100g",
                onClick = { navigateToProduct(product.id) },
            ) {
                // TODO: Display image rather than a block colour.
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }

    if (editDialogOpen) {
        EditCategoryDialog(
            onSave = viewModel::updateCategory,
            onClose = { editDialogOpen = false }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun CategoryTopBar(
    categoryName: String,
    editCategory: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(text = categoryName) },
        actions = {
            IconButton(onClick = editCategory) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.category_edit)
                )
            }
        },
        scrollBehavior = scrollBehaviour
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditCategoryDialog(
    onSave: (String) -> Unit,
    onClose: () -> Unit,
) {
    var categoryName by rememberSaveable { mutableStateOf(value = "") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    AlertDialog(
        title = { Text(text = "Edit Category") },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(categoryName)
                    onClose()
                    categoryName = ""
                }
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClose()
                    categoryName = ""
                }
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column {
                Text(
                    text = "Rename the product category.",
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .focusRequester(focusRequester),
                    label = { Text(text = stringResource(R.string.category_name)) },
                    value = categoryName,
                    onValueChange = { categoryName = it },
                    singleLine = true
                )
            }
        },
        onDismissRequest = onClose
    )
}