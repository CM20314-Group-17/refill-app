package uk.ac.bath.cm20314.refill.ui.categories

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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.category.CategoryDialog
import uk.ac.bath.cm20314.refill.ui.common.RefillCard
import uk.ac.bath.cm20314.refill.ui.common.RefillList
import uk.ac.bath.cm20314.refill.ui.common.Thumbnail

/** Displays a list of product categories. */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CategoriesScreen(
    navigateToCategory: (Category) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToSearch: () -> Unit,
    viewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
) {
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    var createDialogOpen by rememberSaveable { mutableStateOf(value = false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.messages.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    RefillLayout(
        topBar = { scrollBehaviour ->
            CategoriesTopBar(
                navigateToSettings = navigateToSettings,
                navigateToSearch = navigateToSearch,
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
                    contentDescription = stringResource(R.string.categories_add)
                )
            }
        },
        snackbarHostState = snackbarHostState
    ) {
        RefillList(items = categories) { category ->
            RefillCard(
                title = category.categoryName,
                label = pluralStringResource(
                    R.plurals.categories_items,
                    category.itemCount,
                    category.itemCount
                ),
                onClick = { navigateToCategory(category) }
            ) {
                Thumbnail(
                    thumbnail = category.thumbnail,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

    CategoryDialog(
        visible = createDialogOpen,
        heading = { Text(text = "Create category") },
        onClose = { createDialogOpen = false },
        onSave = viewModel::createCategory,
        category = Category()
    )
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
                    contentDescription = stringResource(R.string.categories_settings),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        scrollBehavior = scrollBehaviour
    )
}