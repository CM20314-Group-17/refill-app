package uk.ac.bath.cm20314.refill.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
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
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.common.RefillCard
import uk.ac.bath.cm20314.refill.ui.common.RefillList

/** Displays a list of products within a category. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryName: String,
    navigateToProduct: (Product) -> Unit,
    navigateBack: () -> Unit,
    viewModel: CategoryViewModel = viewModel(factory = CategoryViewModel.Factory(categoryName))
) {
    var editDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    var deleteDialogOpen by rememberSaveable { mutableStateOf(value = false) }

    val products by viewModel.products.collectAsState()
    val category by viewModel.category.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
                categoryName = category?.categoryName ?: "",
                editCategory = { editDialogOpen = true },
                onDeleteCategory = { deleteDialogOpen = true },
                navigateBack = navigateBack,
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
                title = product.productName,//THIS USED TO SAY PRODUCT.NAME
                label = "${product.pricePerKg}p / 100g",
                onClick = { navigateToProduct(product) }
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

    if (deleteDialogOpen) {
        DeleteCategoryDialog(
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteCategory().join()
                    navigateBack()
                }
            },
            onClose = { deleteDialogOpen = false }
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun CategoryTopBar(
    categoryName: String,
    editCategory: () -> Unit,
    onDeleteCategory: () -> Unit,
    navigateBack: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    var dropdownOpen by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(text = categoryName) },
        navigationIcon = {
            IconButton(onClick = {
                navigateBack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = editCategory) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.category_edit)
                )
            }
            IconButton(onClick = { dropdownOpen = !dropdownOpen }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = dropdownOpen,
                onDismissRequest = { dropdownOpen = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Delete category") },
                    onClick = {
                        onDeleteCategory()
                        dropdownOpen = false
                    }
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
                Text(text = "Rename the product category.")
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

@Composable
private fun DeleteCategoryDialog(
    onDelete: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        title = { Text(text = "Permanently delete?") },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text(text = "Cancel")
            }
        },
        text = { Text(text = "Deleting this category will also remove all its products from the database.") },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null
            )
        },
        onDismissRequest = onClose
    )
}