package uk.ac.bath.cm20314.refill.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.CategoryRepositoryImpl
import uk.ac.bath.cm20314.refill.data.category.defaultCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.ProductRepositoryImpl

class CategoriesViewModel(
    private val repository: CategoryRepository
    ) : ViewModel() {

    enum class Event {
        CategoryCreated
    }
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val _categories = MutableStateFlow(emptyList<Category>())
    val categories = _categories.asStateFlow()

    private var category: Category? = null

    /** Loads categories from the repository. */
    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
            ProductRepositoryImpl.getProduct("Mango","Dried Fruit")
            //ProductRepositoryImpl.createProduct("Dried Fruit", "3", "Pineapple", 13, 100f, true)
            //CategoryRepositoryImpl.createCategory("Nuts")
            //ProductRepositoryImpl.createProduct("Nuts", "0", "Cashew", 7, 100f, true)
            ProductRepositoryImpl.getProduct("3", "Dried Fruit")
        }
    }

    /** Creates a new category with a particular name. */
    fun createCategory(name: String) {
        viewModelScope.launch {
            category = repository.createCategory(name)
            loadCategories()
            _events.send(Event.CategoryCreated)
        }
    }

    /** Deletes the previously created category. */
    fun undoCreateCategory() {
        viewModelScope.launch {
            category?.let { category ->
                repository.deleteCategory(category.categoryName)
                loadCategories()
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CategoriesViewModel(defaultCategoryRepository)
            }
        }
    }
}