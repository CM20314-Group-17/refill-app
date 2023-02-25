package uk.ac.bath.cm20314.refill.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl

class CategoryViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow(listOf<Product>())

    val products = _products.asStateFlow()

    init {
        viewModelScope.launch {
            _products.value = repository.getProducts("test")
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CategoryViewModel(ProductRepositoryImpl)
            }
        }
    }
}