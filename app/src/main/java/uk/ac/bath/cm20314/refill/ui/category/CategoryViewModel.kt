package uk.ac.bath.cm20314.refill.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
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

    enum class Event {
        CategoryUpdated
    }

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val category = categoryRepository.getCategory(categoryId)
    val products = productRepository.getProducts(categoryId)

    fun updateCategory(category: Category) {
        categoryRepository.updateCategory(category)
        _events.trySend(Event.CategoryUpdated)
    }

    fun createProduct(product: Product) {
        productRepository.createProduct(product)
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