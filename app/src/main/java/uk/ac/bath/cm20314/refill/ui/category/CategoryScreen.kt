package uk.ac.bath.cm20314.refill.ui.category

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.common.RefillCard
import uk.ac.bath.cm20314.refill.ui.common.RefillList
import uk.ac.bath.cm20314.refill.ui.common.Thumbnail
import uk.ac.bath.cm20314.refill.ui.product.ProductDialog

/** Displays a list of products within a category. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryId: String,
    navigateToProduct: (Product) -> Unit,
    navigateBack: () -> Unit,
    viewModel: CategoryViewModel = viewModel(factory = CategoryViewModel.Factory(categoryId))
) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val category by viewModel.category.collectAsState(initial = null)

    var editDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    var deleteDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    var createDialogOpen by rememberSaveable { mutableStateOf(value = false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messages.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
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
            FloatingActionButton(
                onClick = { createDialogOpen = true },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
            ) {
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
                title = product.productName,
                label = stringResource(R.string.product_price, product.pricePerKg.toFloat() / 100),
                onClick = { navigateToProduct(product) },
                bubble = !product.isUpdated
            ) {
                Thumbnail(
                    thumbnail = product.thumbnail,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

    CategoryDialog(
        visible = editDialogOpen,
        heading = { Text(text = "Edit category") },
        onClose = { editDialogOpen = false },
        onSave = viewModel::updateCategory,
        category = category ?: Category()
    )

    ProductDialog(
        visible = createDialogOpen,
        heading = { Text(text = "Create product") },
        onClose = { createDialogOpen = false },
        onSave = viewModel::createProduct,
        product = Product(categoryId = categoryId)
    )

    if (deleteDialogOpen) {
        DeleteCategoryDialog(
            onDelete = {
                viewModel.deleteCategory()
                navigateBack()
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

    TopAppBar(
        title = {
            Text(
                text = categoryName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = editCategory) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.category_edit),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = { dropdownOpen = !dropdownOpen }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
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