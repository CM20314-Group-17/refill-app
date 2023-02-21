package uk.ac.bath.cm20314.refill.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.ui.dataStore
import uk.ac.bath.cm20314.refill.ui.rememberDarkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigateBack: () -> Unit) {
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            SettingsTopBar(
                navigateBack = navigateBack,
                scrollBehaviour = scrollBehaviour
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ThemeSelection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(
    navigateBack: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.settings_title))
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.settings_back)
                )
            }
        },
        scrollBehavior = scrollBehaviour
    )
}

@Composable
private fun ThemeSelection() {
    Column {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val darkTheme by rememberDarkTheme()

        Text(
            text = stringResource(R.string.settings_theme),
            modifier = Modifier.padding(16.dp)
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