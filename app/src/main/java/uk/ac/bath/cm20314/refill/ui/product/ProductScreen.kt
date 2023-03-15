package uk.ac.bath.cm20314.refill.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.nfc.LocalNfc
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.common.Thumbnail
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    categoryId: String,
    productId: String,
    navigateBack: () -> Unit,
    viewModel: ProductViewModel = viewModel(factory = ProductViewModel.Factory(categoryId, productId))
) {
    val product by viewModel.product.collectAsState(initial = null)

    var editDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    var deleteDialogOpen by rememberSaveable { mutableStateOf(value = false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val nfcCoroutineScope = rememberCoroutineScope()
    val nfcRepository = LocalNfc.current
    var nfcDialogOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                ProductViewModel.Event.ProductUpdated -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Product renamed",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        TODO()
                    }
                }
            }
        }
    }

    RefillLayout(
        topBar = { scrollBehaviour ->
            ProductTopBar(
                productName = product?.productName ?: "",
                editProduct = { editDialogOpen = true },
                onDeleteProduct = { deleteDialogOpen = true },
                navigateBack = navigateBack,
                scrollBehaviour = scrollBehaviour
            )
        },
        actions = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.product_nfc)) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.sharp_nfc_24),
                        contentDescription = stringResource(R.string.product_nfc)
                    )
                },
                onClick = {
                    nfcDialogOpen = true
                    product?.let { product ->
                        nfcCoroutineScope.launch {
                            nfcRepository.writeProductInformation(product)
                            nfcDialogOpen = false
                        }
                    }
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
            )
        },
        snackbarHostState = snackbarHostState
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Thumbnail(
                thumbnail = product?.thumbnail ?: 0,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(16.dp)) {
                val price = product?.let { it.pricePerKg.toFloat() / 100 } ?: 0f
                val portion = product?.portionSize?.roundToInt()
                val changes = product?.isUpdated == false

                Text(
                    text = stringResource(id = R.string.product_price_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(R.string.product_price, price),
                    style = MaterialTheme.typography.titleLarge,
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Row {
                    Text(
                        text = stringResource(R.string.product_portion_size),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${portion}g",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Row {
                    Text(
                        text = stringResource(R.string.product_changes_pending),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (changes) "Yes" else "No",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Divider(modifier = Modifier.padding(top = 16.dp, bottom = 72.dp))
            }
        }
    }

    ProductDialog(
        visible = editDialogOpen,
        heading = { Text(text = "Edit product") },
        onClose = { editDialogOpen = false },
        onSave = viewModel::updateProduct,
        product = product ?: Product()
    )

    if (deleteDialogOpen) {
        DeleteProductDialog(
            onDelete = {
                viewModel.deleteProduct()
                navigateBack()
            },
            onClose = { deleteDialogOpen = false }
        )
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

@ExperimentalMaterial3Api
@Composable
private fun ProductTopBar(
    productName: String,
    editProduct: () -> Unit,
    onDeleteProduct: () -> Unit,
    navigateBack: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    var dropdownOpen by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = productName) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = editProduct) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.product_edit),
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
                    text = { Text(text = "Delete product") },
                    onClick = {
                        onDeleteProduct()
                        dropdownOpen = false
                    }
                )
            }
        },
        scrollBehavior = scrollBehaviour
    )
}

@Composable
private fun DeleteProductDialog(
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
        text = { Text(text = "Deleting this product will remove it from the database.") },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null
            )
        },
        onDismissRequest = onClose
    )
}