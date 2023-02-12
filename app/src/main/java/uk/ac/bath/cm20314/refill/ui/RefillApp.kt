package uk.ac.bath.cm20314.refill.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.bath.cm20314.refill.ui.theme.RefillTheme

/** Contains the app's entire user interface. */
@Composable
fun RefillApp() {
    RefillTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Text(text = "hello world")
        }
    }
}