package uk.ac.bath.cm20314.refill.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.product.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navigateBack: () -> Unit,
    navigateToProduct: (Product) -> Unit,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState(initial = emptyList())

    Surface(
        modifier = Modifier.fillMaxSize(),
        tonalElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Row(
                modifier = Modifier.height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigateBack,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.search_back)
                    )
                }
                SearchInput(
                    query = query,
                    onQueryChange = viewModel::onQueryChange
                )
            }
            Divider()
            LazyColumn {
                items(results) { product ->
                    ListItem(
                        headlineText = { Text(text = product.name) },
                        modifier = Modifier.clickable { navigateToProduct(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchInput(
    query: String,
    onQueryChange: (String) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box {
        val label = stringResource(R.string.search_input)

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .semantics { contentDescription = label },
            textStyle = TextStyle(MaterialTheme.colorScheme.onSurface) + MaterialTheme.typography.bodyLarge,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
        )

        if (query.isEmpty()) {
            Text(
                text = "Search for products",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}