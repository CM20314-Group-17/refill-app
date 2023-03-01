package uk.ac.bath.cm20314.refill.ui.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.CategoryRepositoryImpl
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.data.product.ProductRepository
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl

class CategoryViewModel(
    categoryId: String,
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
            _category.value = categoryRepository.getCategory(categoryId)
            _products.value = productRepository.getProducts(categoryId)
        }
    }

    fun updateCategory(name: String) {
        viewModelScope.launch {
            _category.update { category ->
                previousName = category?.name
                category?.copy(name = name) ?: category
            }
            _category.value?.let { categoryRepository.updateCategory(it) }
            _events.send(Event.CategoryUpdated)
        }
    }

    fun undoUpdateCategory() {
        viewModelScope.launch {
            // TODO: Fix problem that the title doesn't update when undoing rename.
            _category.value = category.value?.copy(name = previousName!!)
            _category.value?.let { categoryRepository.updateCategory(it) }
        }
    }

    class Factory(private val categoryId: String) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            CategoryViewModel(categoryId, CategoryRepositoryImpl, ProductRepositoryImpl) as T
    }
}