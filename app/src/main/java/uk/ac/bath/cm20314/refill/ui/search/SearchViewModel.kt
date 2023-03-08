package uk.ac.bath.cm20314.refill.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.CategoryRepositoryImpl
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl

class SearchViewModel(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _query = MutableStateFlow(value = "")
    val query = _query.asStateFlow()

    private val _products = MutableStateFlow(emptyList<Product>())
    val results = _products.asStateFlow().combine(query) { products, query ->
        products.filter { query.isNotBlank() && it.name.contains(query, ignoreCase = true) }
    }

    init {
        viewModelScope.launch {
            val categories = categoryRepository.getCategories()
            _products.value = categories.flatMap { productRepository.getProducts(it.categoryId) }
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                SearchViewModel(CategoryRepositoryImpl, ProductRepositoryImpl)
            }
        }
    }
}