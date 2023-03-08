package uk.ac.bath.cm20314.refill.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.nfc.LocalNfc
import uk.ac.bath.cm20314.refill.ui.RefillLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    categoryName: String,
    productName: String,
    navigateBack: () -> Unit,
    viewModel: ProductViewModel = viewModel(factory = ProductViewModel.Factory(categoryName, productName))
) {
    val product by viewModel.product.collectAsState()

    var editDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    var deleteDialogOpen by rememberSaveable { mutableStateOf(value = false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    //
    // Temporary UI for testing NFC.
    //

    // Don't reuse the NFC coroutine scope since it may be cancelled.
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
                        viewModel.product.collect()
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
        snackbarHostState = snackbarHostState
    ) {
        Column(Modifier.fillMaxSize().padding(30.dp).background(Color.Red) ){
            Text(text = stringResource(id = R.string.product_price_by_weight), fontStyle = FontStyle.Italic, fontSize = 20.sp)
            Text(text = ((product?.pricePerKg ?: "").toString()+"p/100g"), fontWeight = FontWeight.Bold, fontSize = 60.sp)

            Row(Modifier.weight(1f).padding(0.dp).background(Color.Green)) {
                Column(Modifier.fillMaxWidth().weight(1f).padding(0.dp).background(Color.Blue)) {
                    Text(
                        text = stringResource(id = R.string.product_portion_size),
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                    )
                    Text(
                        text = stringResource(id = R.string.product_last_updated),
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                    )
                }
                Column(Modifier.fillMaxWidth().weight(1f).padding(0.dp).background(Color.Yellow)) {
                    Text(
                        text = (product?.portionSize ?: "None").toString(),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Right,
                    )
                    // TODO: use isupdated bool to set this i suppose
                    Text(
                        text = "03/11/2002",
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Right,
                    )
                }
            }

            IconButton(
                modifier = Modifier.size(128.dp).background(Color.Black),
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
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = stringResource(R.string.product_nfc)
                )
            }
        }
    }

    if (editDialogOpen) {
        EditProductDialog(
            onSave = viewModel::updateProduct,
            onClose = { editDialogOpen = false }
        )
    }

    if (deleteDialogOpen) {
        DeleteProductDialog(
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteProduct().join()
                    navigateBack()
                }
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

    CenterAlignedTopAppBar(
        title = { Text(text = productName) },
        navigationIcon = {
            IconButton(onClick = {
                navigateBack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = editProduct) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.product_edit)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProductDialog(
    onSave: (String) -> Unit,
    onClose: () -> Unit,
) {
    var productName by rememberSaveable { mutableStateOf(value = "") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    AlertDialog(
        title = { Text(text = "Edit Product") },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(productName)
                    onClose()
                    productName = ""
                }
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClose()
                    productName = ""
                }
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column {
                Text(text = "Rename the product.")
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .focusRequester(focusRequester),
                    label = { Text(text = stringResource(R.string.product_name)) },
                    value = productName,
                    onValueChange = { productName = it },
                    singleLine = true
                )
            }
        },
        onDismissRequest = onClose
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