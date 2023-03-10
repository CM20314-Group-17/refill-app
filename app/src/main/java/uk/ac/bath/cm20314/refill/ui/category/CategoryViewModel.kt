package uk.ac.bath.cm20314.refill.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.defaultCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.defaultProductRepository

class CategoryViewModel(
    categoryName: String,
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    enum class Event {
        CategoryUpdated
    }
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _category = MutableStateFlow<Category?>(null)
    val category = _category.asStateFlow()

    private val _products = MutableStateFlow(emptyList<Product>())
    val products = _products.asStateFlow()

    private var previousName: String? = null

    init {
        viewModelScope.launch {
            _category.value = categoryRepository.getCategory(categoryName)
            _products.value = productRepository.getProducts(categoryName)
        }
    }

    fun updateCategory(name: String) {
        viewModelScope.launch {
            _category.value?.let { category ->
                previousName = category.categoryName
                categoryRepository.updateCategory(category, name)
            }
            _category.value = _category.value?.copy(categoryName = name)
            _events.send(Event.CategoryUpdated)
        }
    }

    fun undoUpdateCategory() {
        viewModelScope.launch {
            // TODO: Fix problem that the title doesn't update when undoing rename.
            _category.value?.let { categoryRepository.updateCategory(it, previousName!!) }
            _category.value = _category.value?.copy(categoryName = previousName!!)
        }
    }

    fun deleteCategory() = viewModelScope.launch {
        _category.value?.let { categoryRepository.deleteCategory(it.categoryName) }
    }

    class Factory(private val categoryName: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            CategoryViewModel(categoryName, defaultCategoryRepository, defaultProductRepository) as T
    }
}