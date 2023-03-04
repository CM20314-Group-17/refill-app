package uk.ac.bath.cm20314.refill.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.collectLatest
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.common.RefillCard
import uk.ac.bath.cm20314.refill.ui.common.RefillList
import uk.ac.bath.cm20314.refill.ui.common.SearchDialog

/** Displays a list of product categories. */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CategoriesScreen(
    navigateToCategory: (categoryId: String) -> Unit,
    navigateToSettings: () -> Unit,
    viewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
) {
    var searchDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    var createDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    var searchText by rememberSaveable { mutableStateOf(value = "") }
    val categories by viewModel.categories.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
        viewModel.events.collectLatest { event ->
            when (event) {
                CategoriesViewModel.Event.CategoryCreated -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Category created",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.undoCreateCategory()
                    }
                }
            }
        }
    }

    RefillLayout(
        topBar = { scrollBehaviour ->
            CategoriesTopBar(
                navigateToSearch = {
                    searchText = ""
                    searchDialogOpen = true
                },
                navigateToSettings = navigateToSettings,
                scrollBehaviour = scrollBehaviour
            )
        },
        actions = {
            FloatingActionButton(onClick = { createDialogOpen = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.categories_add)
                )
            }
        },
        snackbarHostState = snackbarHostState
    ) {
        RefillList(items = categories) { category ->
            val count = category.itemCount

            RefillCard(
                title = category.name,
                label = pluralStringResource(R.plurals.categories_items, count, count),
                onClick = { navigateToCategory(category.id) }
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

    SearchDialog(
        active = searchDialogOpen,
        query = searchText,
        placeholder = stringResource(R.string.search_placeholder),
        onClose = { searchDialogOpen = false },
        onQueryChange = { searchText = it }
    ) {
        // TODO: Add search results.
    }

    if (createDialogOpen) {
        CreateCategoryDialog(
            createCategory = viewModel::createCategory,
            onClose = { createDialogOpen = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoriesTopBar(
    navigateToSearch: () -> Unit,
    navigateToSettings: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.categories_title)) },
        navigationIcon = {
            IconButton(onClick = navigateToSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.categories_search)
                )
            }
        },
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.categories_settings)
                )
            }
        },
        scrollBehavior = scrollBehaviour
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCategoryDialog(
    createCategory: (name: String) -> Unit,
    onClose: () -> Unit,
) {
    var categoryName by rememberSaveable { mutableStateOf(value = "") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    AlertDialog(
        title = { Text(text = stringResource(R.string.category_new)) },
        confirmButton = {
            TextButton(
                onClick = {
                    createCategory(categoryName)
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
                Text(text = "Create a new product category.")
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
        onDismissRequest = {
            onClose()
            categoryName = ""
        }
    )
}