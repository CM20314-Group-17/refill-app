package uk.ac.bath.cm20314.refill.ui.common

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import uk.ac.bath.cm20314.refill.R

/**
 * A fullscreen model with search bar and area to display search results.
 *
 * @param active whether the search modal is open.
 * @param query the search query to display in the search bar.
 * @param placeholder the placeholder text to display if the search bar is empty.
 * @param onActiveChange a callback for when the search bar should open or close.
 * @param onQueryChange a callback for when the user inputs text into the search bar.
 * @param content the content to display in the search results area.
 */
@Composable
fun SearchModal(
    active: Boolean,
    query: String,
    placeholder: String,
    onActiveChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    content: @Composable () -> Unit
) {
    ExpandAnimation(
        visible = active,
        modifier = Modifier.zIndex(1f)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onActiveChange(false) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.search_back)
                        )
                    }
                    SearchInput(
                        query = query,
                        placeholder = placeholder,
                        onQueryChange = onQueryChange,
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider()
                content()
            }
        }

        // Close the search bar when the user presses the back button.
        BackHandler {
            onActiveChange(false)
        }
    }
}

@Composable
private fun ExpandAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val initialSize = LocalDensity.current.run {
            IntSize(
                width = (maxWidth - 64.dp).roundToPx(),
                height = 48.dp.roundToPx()
            )
        }

        AnimatedVisibility(
            visible = visible,
            content = content,
            modifier = Modifier.align(Alignment.TopCenter),
            enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter) { initialSize },
            exit = fadeOut(tween(durationMillis = 200))
        )
    }
}

@Composable
private fun SearchInput(
    query: String,
    placeholder: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        val label = stringResource(R.string.search_input)
        val focusRequester = remember { FocusRequester() }

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            modifier = Modifier
                .focusRequester(focusRequester)
                .semantics { contentDescription = label },
            textStyle = TextStyle(MaterialTheme.colorScheme.onSurface) + MaterialTheme.typography.bodyLarge,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
        )

        // Display the placeholder text underneath the search input.
        // The placeholder disappears if the user has inputted any text.
        if (query.isEmpty()) {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Automatically open the keyboard.
        // Only run when the search input enters the screen.
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}