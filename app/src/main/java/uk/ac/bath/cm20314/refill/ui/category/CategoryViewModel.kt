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
        categoryRepository.updateCategory(category)
    }

    fun createProduct(product: Product) {
        if (product.productName.isBlank()) {
            return
        }
        viewModelScope.launch {
            productRepository.createProduct(product).also { success ->
                if (!success) {
                    channel.send("Product already exists")
                }
            }
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