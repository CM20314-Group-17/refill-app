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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.ui.common.RefillCard
import uk.ac.bath.cm20314.refill.ui.common.RefillList
import uk.ac.bath.cm20314.refill.ui.common.SearchModal

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
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // The Scaffold arranges the top app bar and floating action button.
    // It is currently in the experimental material 3 API, which requires the '@OptIn' annotation.
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CategoriesTopBar(
                navigateToSearch = {
                    categoriesViewModel.updateSearchResults(search = "")
                    searchOpen = true
                },
                navigateToSettings = navigateToSettings,
                scrollBehaviour = scrollBehaviour
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.categories_add)
                )
            }
        }
    ) { padding ->

        // Display the list of categories from the view model.
        // 'collectAsState' will cause the UI to automatically update when the data changes.
        val categories by categoriesViewModel.categories.collectAsState()
        RefillList(
            items = categories,
            modifier = Modifier.padding(padding)
        ) { category ->

            // Display each category in a card containing its name and the number of items.
            // Each card navigates to the category screen when the user clicks it.
            RefillCard(
                title = category.name,
                label = if (category.itemCount == 1) "1 item" else "${category.itemCount} items",
                onClick = { navigateToCategory(category.id) },
                preview = {
                    // TODO: Update the preview to show an image instead of a block colour.
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            )
        }
    }

    val searchText by categoriesViewModel.searchText.collectAsState()

    SearchModal(
        active = searchOpen,
        query = searchText,
        placeholder = stringResource(R.string.search_placeholder),
        onActiveChange = { searchOpen = it },
        onQueryChange = categoriesViewModel::updateSearchResults
    ) {
        // TODO: Add list of search results.
    }
}

@ExperimentalMaterial3Api
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