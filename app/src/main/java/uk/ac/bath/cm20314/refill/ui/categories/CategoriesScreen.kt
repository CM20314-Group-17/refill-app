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

/** Displays a list of product categories. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navigateToCategory: (categoryId: String) -> Unit,
    navigateToSettings: () -> Unit,
    categoriesViewModel: CategoriesViewModel = viewModel()
) {
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var searchOpen by rememberSaveable { mutableStateOf(false) }

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
        val categories by categoriesViewModel.categories.collectAsState()

        RefillList(
            items = categories,
            modifier = Modifier.padding(padding)
        ) { category ->
            RefillCard(
                title = category.name,
                label = if (category.itemCount == 1) "1 item" else "${category.itemCount} items",
                onClick = { navigateToCategory(category.id) },
                preview = {
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
        // ...
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