package uk.ac.bath.cm20314.refill.ui.category

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

/** Displays a list of products within a category. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryId: String,
    navigateToProduct: (productId: String) -> Unit,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
//  var searchOpen by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CategoryTopBar(
//                navigateToSearch = {
//                    categoryViewModel.updateSearchResults(search = "")
//                    searchOpen = true
//                },
                categoryId = categoryId,
                scrollBehaviour = scrollBehaviour
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.category_add)
                )
            }
        }
    ) { padding ->
        val products by categoryViewModel.products.collectAsState()

        RefillList(
            items = products,
            modifier = Modifier.padding(padding)
        ) { product ->
            RefillCard(
                title = product.name,
                label = "${product.price_per_kg}p / 100g",
                onClick = { navigateToProduct(product.id) },
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

//    val searchText by categoryViewModel.searchText.collectAsState()
//
//    SearchModal(
//        active = searchOpen,
//        query = searchText,
//        placeholder = stringResource(R.string.search_placeholder),
//        onActiveChange = { searchOpen = it },
//        onQueryChange = categoryViewModel::updateSearchResults
//    ) {
//        // ...
//    }
}

@ExperimentalMaterial3Api
@Composable
private fun CategoryTopBar(
    //navigateToSearch: () -> Unit,
    categoryId: String,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = categoryId)
        },

        // ToDo: Add back button

// Do we want search within category? It's not in the design

//        navigationIcon = {
//            IconButton(onClick = navigateToSearch) {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = stringResource(R.string.category_search)
//                )
//            }
//        },

        scrollBehavior = scrollBehaviour
    )
}

@Preview
@Composable
fun CategoryScreenPreview() {
    CategoryScreen(
        navigateToProduct = {},
        categoryId = "testCategory",
    )
}