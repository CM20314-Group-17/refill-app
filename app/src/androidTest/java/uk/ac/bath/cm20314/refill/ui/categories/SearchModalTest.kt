package uk.ac.bath.cm20314.refill.ui.categories

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.bath.cm20314.refill.R
import uk.ac.bath.cm20314.refill.ui.common.SearchModal

@RunWith(AndroidJUnit4::class)
class SearchModalTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            var open by remember { mutableStateOf(true) }
            var text by remember { mutableStateOf("") }

            SearchModal(
                active = open,
                query = text,
                placeholder = "placeholder",
                onActiveChange = { open = it },
                onQueryChange = { text = it },
                content = {}
            )
        }
    }

    /** Checks that the search bar closes. */
    @Test
    fun testClose() {
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.search_back))
            .performClick()
        composeTestRule.onNodeWithText(text = "placeholder").assertDoesNotExist()
    }

    /** Checks that search bar automatically focuses. */
    @Test
    fun testFocused() {
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.search_input))
            .assertIsFocused()
    }

    /** Checks that the search bar responds to text input. */
    @Test
    fun testSearchInput() {
        val label = composeTestRule.activity.getString(R.string.search_input)
        composeTestRule.onNodeWithContentDescription(label).performTextInput(text = "test")
        composeTestRule.onNodeWithContentDescription(label).assert(hasText(text = "test"))
    }

    /** Checks that the placeholder disappears on input. */
    @Test
    fun testPlaceholder() {
        composeTestRule.onNodeWithText(text = "placeholder").assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.search_input))
            .performTextInput(text = "test")
        composeTestRule.onNodeWithText(text = "placeholder").assertDoesNotExist()
    }
}