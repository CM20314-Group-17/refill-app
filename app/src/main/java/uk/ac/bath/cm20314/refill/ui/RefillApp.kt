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

val Context.dataStore by preferencesDataStore(name = "preferences")

/** Contains the app's entire user interface. */
@Composable
fun RefillApp() {
    val darkTheme by rememberDarkTheme()

    RefillTheme(darkTheme = darkTheme ?: isSystemInDarkTheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavGraph()
        }
    }
}

/** Returns the selected theme, or null if the user has not selected a theme. */
@Composable
fun rememberDarkTheme(): State<Boolean?> {
    val context = LocalContext.current

    return remember {
        context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(name = "darkTheme")]
        }
    }.collectAsState(initial = null)
}