package uk.ac.bath.cm20314.refill.ui.categories

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.ac.bath.cm20314.refill.data.Category

class CategoriesViewModel : ViewModel() {

    private val _categories = MutableStateFlow(testCategories)
    private val _searchText = MutableStateFlow("")

    val categories = _categories.asStateFlow()
    val searchText = _searchText.asStateFlow()

    fun updateSearchResults(search: String) {
        _searchText.value = search
    }
}

private val testCategories = listOf(
    Category(id = "test", name = "Dried Fruits", itemCount = 9),
    Category(id = "test", name = "Pasta", itemCount = 8),
    Category(id = "test", name = "Nuts & Seeds", itemCount = 9),
    Category(id = "test", name = "Rice", itemCount = 5),
    Category(id = "test", name = "Beans", itemCount = 4)
)