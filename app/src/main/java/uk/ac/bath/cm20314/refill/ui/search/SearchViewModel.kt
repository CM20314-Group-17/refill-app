package uk.ac.bath.cm20314.refill.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.CategoryRepositoryImpl
import uk.ac.bath.cm20314.refill.data.category.defaultCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl
import uk.ac.bath.cm20314.refill.data.product.defaultProductRepository

class SearchViewModel(
    productRepository: ProductRepository
) : ViewModel() {

    private val _query = MutableStateFlow(value = "")
    val query = _query.asStateFlow()

    private val _products = productRepository.getAllProducts()
    val results = _products.combine(query) { products, query ->
        products.filter { query.isNotBlank() && it.productName.contains(query, ignoreCase = true) }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                SearchViewModel(defaultProductRepository)
            }
        }
    }
}