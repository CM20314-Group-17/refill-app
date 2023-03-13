package uk.ac.bath.cm20314.refill.ui.common

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FullscreenDialog(
    visible: Boolean,
    onClose: () -> Unit,
    heading: @Composable () -> Unit,
    actions: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    BackHandler(
        enabled = visible,
        onBack = onClose
    )
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val initialSizePixels = LocalDensity.current.run {
            IntSize(
                width = (maxWidth - 64.dp).roundToPx(),
                height = (maxHeight - 64.dp).roundToPx()
            )
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + expandIn(expandFrom = Alignment.TopCenter) { initialSizePixels },
            exit = fadeOut(spring(stiffness = Spring.StiffnessMedium))
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column {
                    DialogHeader(
                        onClose = onClose,
                        heading = heading,
                        actions = actions
                    )
                    val alpha by transition.animateFloat(
                        label = "",
                        transitionSpec = { tween(delayMillis = 100) },
                        targetValueByState = { if (it == EnterExitState.Visible) 1f else 0f }
                    )
                    val offset by transition.animateDp(
                        label = "",
                        transitionSpec = { tween(delayMillis = 100, easing = EaseOutExpo) },
                        targetValueByState = { if (it == EnterExitState.Visible) 0.dp else 32.dp }
                    )
                    Column(
                        modifier = Modifier
                            .padding(top = offset)
                            .padding(horizontal = 16.dp)
                            .alpha(alpha)
                            .navigationBarsPadding()
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogHeader(
    onClose: () -> Unit,
    heading: @Composable () -> Unit,
    actions: @Composable () -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            .statusBarsPadding()
            .height(64.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleLarge) {
            Box(modifier = Modifier.weight(1f)) {
                heading()
            }
        }
        actions()
        Spacer(if (isLandscape) Modifier.navigationBarsPadding() else Modifier.width(12.dp))
    }
}