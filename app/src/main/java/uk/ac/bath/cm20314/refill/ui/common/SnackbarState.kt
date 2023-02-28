package uk.ac.bath.cm20314.refill.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalSnackbarState = staticCompositionLocalOf<SnackbarState> {
    error("No SnackbarState provided")
}

class SnackbarState(
    val hostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        onActionPerformed: () -> Unit = {}
    ) {
        coroutineScope.launch {
            if (hostState.showSnackbar(message, actionLabel) == SnackbarResult.ActionPerformed) {
                onActionPerformed()
            }
        }
    }
}

@Composable
fun rememberSnackbarState(): SnackbarState {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    return remember(snackbarHostState, coroutineScope) {
        SnackbarState(
            hostState = snackbarHostState,
            coroutineScope = coroutineScope
        )
    }
}