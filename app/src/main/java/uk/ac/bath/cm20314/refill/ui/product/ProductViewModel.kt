package uk.ac.bath.cm20314.refill.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.defaultProductRepository

class ProductViewModel(
    private val categoryName: String,
    private val productName: String,
    private val productRepository: ProductRepository
) : ViewModel() {

    enum class Event {
        ProductUpdated
    }

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val product = productRepository.getProduct(categoryName, productName)

    private var previousName: String? = null
    private var previousPPK: Int? = null
    private var previousPortion: Float? = null

    fun updateProduct(name: String, ppk: Int, portion: Float) {
        TODO()
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
        productRepository.deleteProduct(categoryName = categoryName, productName = productName)
    }

    class Factory(
        private val categoryName: String,
        private val productName: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            ProductViewModel(categoryName, productName, defaultProductRepository) as T
    }
}