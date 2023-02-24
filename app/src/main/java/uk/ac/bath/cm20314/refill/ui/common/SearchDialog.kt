package uk.ac.bath.cm20314.refill.ui.common

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import uk.ac.bath.cm20314.refill.R

/** A fullscreen dialog with a search bar and an area to display search results. */
@Composable
fun SearchDialog(
    active: Boolean,
    query: String,
    placeholder: String,
    onClose: () -> Unit,
    onQueryChange: (String) -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = active) {
        onClose()
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val initialSizePixels = LocalDensity.current.run {
            IntSize(
                width = (maxWidth - 64.dp).roundToPx(),
                height = 48.dp.roundToPx()
            )
        }

        AnimatedVisibility(
            visible = active,
            enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter) { initialSizePixels },
            exit = fadeOut(spring(stiffness = Spring.StiffnessMedium))
        ) {
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
                            onClick = { onClose() },
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
                            onQueryChange = onQueryChange
                        )
                    }
                    Divider()
                    content()
                }
            }
        }
    }
}

@Composable
private fun SearchInput(
    query: String,
    placeholder: String,
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
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}