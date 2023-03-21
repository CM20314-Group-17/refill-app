package uk.ac.bath.cm20314.refill.ui.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uk.ac.bath.cm20314.refill.R

private val thumbnails = listOf(
    null,
    R.drawable.thumbnail_pasta,
    R.drawable.thumbnail_rice,
    R.drawable.thumbnail_nuts,
    R.drawable.thumbnail_fruit,
    R.drawable.thumbnail_chocolate,
)

@Composable
fun ThumbnailPicker(
    thumbnail: Int,
    onSelect: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(thumbnails.size) { index ->
            val interactionSource = remember {
                MutableInteractionSource()
            }
            val alpha by animateFloatAsState(
                targetValue = if (index == thumbnail) 1f else 0f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
            Surface(
                modifier = Modifier
                    .aspectRatio(ratio = 1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onSelect(index) }
                    ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box {
                    Thumbnail(
                        thumbnail = index,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(1f - (alpha * 0.5f))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .alpha(alpha)
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Thumbnail(
    thumbnail: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.primary)) {
        thumbnails.getOrNull(thumbnail)?.let {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(it),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary, BlendMode.Color)
            )
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0x40000000))
            )
        }
    }
}