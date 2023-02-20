package uk.ac.bath.cm20314.refill.ui

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import uk.ac.bath.cm20314.refill.ui.theme.RefillTheme

// Create the data store to save preferences.
// See https://developer.android.com/topic/libraries/architecture/datastore.
val Context.dataStore by preferencesDataStore(name = "preferences")

/** Contains the app's entire user interface. */
@Composable
fun RefillApp() {
    val darkTheme by rememberDarkTheme()

    // Display the user interface in the correct theme.
    // If the user has not selected a theme, it will use the system default.
    RefillTheme(darkTheme = darkTheme ?: isSystemInDarkTheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {

            // The NavGraph contains the app's screens.
            // It swaps the current screen when the user navigates.
            NavGraph()
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