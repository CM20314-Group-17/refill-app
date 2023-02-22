package uk.ac.bath.cm20314.refill.ui.category

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.ac.bath.cm20314.refill.data.Category
import uk.ac.bath.cm20314.refill.data.Product

class CategoryViewModel : ViewModel() {

    private val _products = MutableStateFlow(testProducts)
    private val _searchText = MutableStateFlow("")

    val products = _products.asStateFlow()
    val searchText = _searchText.asStateFlow()

    fun updateSearchResults(search: String) {
        _searchText.value = search
    }
}

// Create dummy product class in data folder
private val testProducts = listOf(
    Product(id = "test", name = "Spaghetti", price_per_kg = 9, portion_size = 100f),
    Product(id = "test", name = "Pennette (White)", price_per_kg = 8, portion_size = 100f),
    Product(id = "test", name = "Pennette (Wholeweat)", price_per_kg = 9, portion_size = 100f),
    Product(id = "test", name = "Tagliatelle", price_per_kg = 5, portion_size = 100f),
    Product(id = "test", name = "Vermicelli Noodles", price_per_kg = 4, portion_size = 100f)
)