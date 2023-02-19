package uk.ac.bath.cm20314.refill.ui.categories

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ac.bath.cm20314.refill.R

@RunWith(AndroidJUnit4::class)
class CategoriesTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            CategoriesScreen(navigateToCategory = {}, navigateToSettings = {})
        }
    }

    /** Checks that the search bar opens. */
    @Test
    fun testOpenSearch() {
        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.categories_search))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.search_placeholder))
            .assertIsDisplayed()
    }
}