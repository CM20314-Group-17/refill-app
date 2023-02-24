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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.common.RefillCard
import uk.ac.bath.cm20314.refill.ui.common.RefillList
import uk.ac.bath.cm20314.refill.ui.common.SearchDialog

/**
 *  Displays a list of product categories. Clicking on a product category will navigate to the
 *  category screen. The user can also search for products and navigate to the settings screen.
 *
 *  @param navigateToCategory the function that navigates to the category screen.
 *  @param navigateToSettings the function that navigates to the settings screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navigateToCategory: (categoryId: String) -> Unit,
    navigateToSettings: () -> Unit,
    categoriesViewModel: CategoriesViewModel = viewModel()
) {
    var searchOpen by rememberSaveable { mutableStateOf(false) }
    val searchText by categoriesViewModel.searchText.collectAsState()

    RefillLayout(
        topBar = { scrollBehaviour ->
            CategoriesTopBar(
                navigateToSearch = {
                    categoriesViewModel.updateSearchResults(search = "")
                    searchOpen = true
                },
                navigateToSettings = navigateToSettings,
                scrollBehaviour = scrollBehaviour
            )
        },
        actions = {
            FloatingActionButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.categories_add)
                )
            }
        }
    ) {
        val categories by categoriesViewModel.categories.collectAsState()

        RefillList(items = categories) { category ->
            RefillCard(
                title = category.name,
                label = if (category.itemCount == 1) "1 item" else "${category.itemCount} items",
                onClick = { navigateToCategory(category.id) },
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
        active = searchOpen,
        query = searchText,
        placeholder = stringResource(R.string.search_placeholder),
        onClose = { searchOpen = false },
        onQueryChange = categoriesViewModel::updateSearchResults
    ) {
        // TODO: Add search results.
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
        title = {
            Text(text = stringResource(R.string.categories_title))
        },
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

@Preview
@Composable
fun CategoriesScreenPreview() {
    CategoriesScreen(
        navigateToCategory = {},
        navigateToSettings = {}
    )
}