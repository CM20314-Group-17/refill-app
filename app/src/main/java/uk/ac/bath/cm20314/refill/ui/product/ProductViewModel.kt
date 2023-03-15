package uk.ac.bath.cm20314.refill.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.defaultProductRepository

class ProductViewModel(
    private val categoryId: String,
    private val productId: String,
    private val productRepository: ProductRepository
) : ViewModel() {

    enum class Event {
        ProductUpdated
    }

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val product = productRepository.getProduct(categoryId, productId)

    private var previousName: String? = null
    private var previousPPK: Int? = null
    private var previousPortion: Float? = null

    fun updateProduct(product: Product) {
        productRepository.updateProduct(product)
        // TODO: Add undo back in with new repository.
//        viewModelScope.launch {
//            _product.update { product ->
//                previousName = product?.productName
//                previousPPK = product?.pricePerKg
//                previousPortion = product?.portionSize
//                product?.copy(productName = name,
//                              pricePerKg = ppk,
//                              portionSize = portion) ?: product
//            }
//            _product.value?.let { productRepository.updateProduct(it) }
//            _events.send(Event.ProductUpdated)
//        }
    }

    fun deleteProduct() {
        productRepository.deleteProduct(categoryId = categoryId, productId = productId)
    }

    class Factory(
        private val categoryId: String,
        private val productId: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            ProductViewModel(categoryId, productId, defaultProductRepository) as T
    }
}