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

    val product = productRepository.getProduct(categoryId, productId)

    fun updateProduct(product: Product) {
        product.isUpdated = false
        productRepository.updateProduct(product)
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