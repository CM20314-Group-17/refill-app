package uk.ac.bath.cm20314.refill.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.data.category.CategoryRepositoryImpl
import uk.ac.bath.cm20314.refill.data.category.FakeCategoryRepository
import uk.ac.bath.cm20314.refill.data.category.defaultCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl
import uk.ac.bath.cm20314.refill.data.product.defaultProductRepository
import uk.ac.bath.cm20314.refill.ui.RefillLayout
import uk.ac.bath.cm20314.refill.ui.dataStore
import uk.ac.bath.cm20314.refill.ui.rememberDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigateToCategories: () -> Unit) {
    BackHandler {
        navigateToCategories()
    }
    RefillLayout(
        topBar = { scrollBehaviour ->
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings_title))
                },
                navigationIcon = {
                    IconButton(onClick = navigateToCategories) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.settings_back)
                        )
                    }
                },
                scrollBehavior = scrollBehaviour
            )
        }
    ) {
        Column {
            ThemeSelection()
            DatabaseToggle()
        }
    }
}

@Composable
private fun ThemeSelection() {
    Column {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val darkTheme by rememberDarkTheme()

        Text(
            text = stringResource(R.string.settings_theme),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.labelLarge
        )
        RadioOption(
            text = stringResource(R.string.settings_theme_light),
            selected = darkTheme == false,
            onClick = {
                coroutineScope.launch {
                    context.dataStore.edit {
                        it[booleanPreferencesKey(name = "darkTheme")] = false
                    }
                }
            }
        )
        RadioOption(
            text = stringResource(R.string.settings_theme_dark),
            selected = darkTheme == true,
            onClick = {
                coroutineScope.launch {
                    context.dataStore.edit {
                        it[booleanPreferencesKey(name = "darkTheme")] = true
                    }
                }
            }
        )
        RadioOption(
            text = stringResource(R.string.settings_theme_system),
            selected = darkTheme == null,
            onClick = {
                coroutineScope.launch {
                    context.dataStore.edit {
                        if (it.contains(booleanPreferencesKey(name = "darkTheme"))) {
                            it.remove(booleanPreferencesKey(name = "darkTheme"))
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun RadioOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun DatabaseToggle() {
    var remoteDatabase by remember {
        mutableStateOf(defaultCategoryRepository == CategoryRepositoryImpl)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Database",
            style = MaterialTheme.typography.labelLarge
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Use remote database",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = remoteDatabase,
                onCheckedChange = { checked ->
                    remoteDatabase = checked
                    if (remoteDatabase) {
                        defaultCategoryRepository = CategoryRepositoryImpl
                        defaultProductRepository = ProductRepositoryImpl
                    } else {
                        defaultCategoryRepository = FakeCategoryRepository
                        defaultProductRepository = FakeProductRepository
                    }
                }
            )
        }
    }
}