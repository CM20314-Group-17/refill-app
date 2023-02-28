package uk.ac.bath.cm20314.refill.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import uk.ac.bath.cm20314.refill.ui.common.LocalSnackbarState
import uk.ac.bath.cm20314.refill.ui.common.rememberSnackbarState
import uk.ac.bath.cm20314.refill.ui.theme.RefillTheme

val Context.dataStore by preferencesDataStore(name = "preferences")

/** Contains the app's entire user interface. */
@Composable
fun RefillApp() {
    val darkTheme by rememberDarkTheme()
    val snackbarState = rememberSnackbarState()

    RefillTheme(darkTheme = darkTheme ?: isSystemInDarkTheme()) {
        Surface {
            Box {
                CompositionLocalProvider(LocalSnackbarState provides snackbarState) {
                    NavGraph()
                }
                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .zIndex(1f),
                    hostState = snackbarState.hostState
                )
            }
        }
    }
}

/** Arranges the top bar and floating action buttons. */
@ExperimentalMaterial3Api
@Composable
fun RefillLayout(
    topBar: @Composable (TopAppBarScrollBehavior) -> Unit,
    actions: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            topBar(scrollBehaviour)
        },
        floatingActionButton = {
            if (actions != null) {
                Column(
                    modifier = if (isLandscape) Modifier.navigationBarsPadding() else Modifier,
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    content = actions,
                )
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}

/** Returns the selected theme, or null if the user has not selected a theme. */
@Composable
fun rememberDarkTheme(): State<Boolean?> {
    val context = LocalContext.current

    // Get the saved theme from the data store.
    // Will be null if the user has not selected a theme.
    return remember {
        context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(name = "darkTheme")]
        }
    }.collectAsState(initial = null)
}