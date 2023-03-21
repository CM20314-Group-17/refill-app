package uk.ac.bath.cm20314.refill.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.defaultCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.defaultProductRepository

class CategoryViewModel(
    private val categoryId: String,
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val channel = Channel<String>()

    val messages = channel.receiveAsFlow()
    val category = categoryRepository.getCategory(categoryId)
    val products = productRepository.getProducts(categoryId)

    fun updateCategory(category: Category) {
        category.isUpdated = false
        categoryRepository.updateCategory(category)
    }

    fun createProduct(product: Product) {
        viewModelScope.launch {
            val message = when {
                product.productName.isBlank() -> "Product must have a name"
                !productRepository.createProduct(product) -> "Product already exists"
                else -> null
            }
            message?.let { channel.send(it) }
        }
    }

    fun deleteCategory() {
        categoryRepository.deleteCategory(categoryId)
    }

    class Factory(private val categoryId: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            CategoryViewModel(categoryId, defaultCategoryRepository, defaultProductRepository) as T
    }
}