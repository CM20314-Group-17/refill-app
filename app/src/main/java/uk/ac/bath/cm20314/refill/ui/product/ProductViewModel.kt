package uk.ac.bath.cm20314.refill.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl

class ProductViewModel(
    productId: String,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(value = null)
    val product = _product.asStateFlow()

    init {
        viewModelScope.launch {
            _product.value = productRepository.getProduct(productId)
        }
    }

    class Factory(private val productId: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            ProductViewModel(productId, ProductRepositoryImpl) as T
    }
}