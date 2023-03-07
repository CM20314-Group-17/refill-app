package uk.ac.bath.cm20314.refill.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository

class ProductViewModel(
    categoryId: String,
    productId: String,
    private val productRepository: ProductRepository
) : ViewModel() {

    enum class Event {
        ProductUpdated
    }
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _product = MutableStateFlow<Product?>(value = null)
    val product = _product.asStateFlow()

    private var previousName: String? = null

    init {
        viewModelScope.launch {
            _product.value = productRepository.getProduct(categoryId, productId)
        }
    }

    fun updateProduct(name: String) {
        viewModelScope.launch {
            _product.update { product ->
                previousName = product?.name
                product?.copy(name = name) ?: product
            }
            _product.value?.let { productRepository.updateProduct(it) }
            _events.send(ProductViewModel.Event.ProductUpdated)
        }
    }

    fun undoUpdateProduct() {
        viewModelScope.launch {
            // TODO: Fix problem that the title doesn't update when undoing rename.
            _product.value = product.value?.copy(name = previousName!!)
            _product.value?.let { productRepository.updateProduct(it) }
        }
    }
    fun deleteProduct() = viewModelScope.launch {
        _product.value?.let { productRepository.deleteProduct(it.categoryId, it.productId) }
    }

    class Factory(
        private val categoryId: String,
        private val productId: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            ProductViewModel(categoryId, productId, FakeProductRepository) as T
    }
}