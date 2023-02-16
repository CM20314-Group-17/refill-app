package uk.ac.bath.cm20314.refill.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.bath.cm20314.refill.ui.theme.RefillTheme

/** Contains the app's entire user interface. */
@Composable
fun RefillApp() {
    RefillTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavGraph()
        }
    }
}

@Preview
@Composable
fun RefillAppPreview() {
    RefillApp()
}