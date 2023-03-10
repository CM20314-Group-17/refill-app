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
            //CategoryRepositoryImpl.createCategory("Nuts")

            //ProductRepositoryImpl.createProduct("Nuts", "Walnuts", 8, 100f, true)
            //ProductRepositoryImpl.createProduct("Nuts", "Almonds", 11, 100f, true)
            //ProductRepositoryImpl.createProduct("Dried Fruit", "Apricots", 10, 100f, true)
            //ProductRepositoryImpl.createProduct("Dried Fruit", "Banana", 5, 100f, true)
            //ProductRepositoryImpl.createProduct("Dried Fruit", "Mango", 15, 100f, true)
            //ProductRepositoryImpl.createProduct("Pasta", "Spaghetti", 9, 100f, true)
            //ProductRepositoryImpl.createProduct("Pasta", "Pennette (White)", 8, 100f, true)
            //ProductRepositoryImpl.createProduct("Pasta", "Pennette (Wholewheat)", 9, 100f, true)
            //ProductRepositoryImpl.createProduct("Pasta", "Tagliatelle", 5, 100f, true)
            //ProductRepositoryImpl.createProduct("Pasta", "Vermicelli Noodles", 4, 100f, true)

            ProductRepositoryImpl.getProduct("Pasta","Spaghetti")
            //ProductRepositoryImpl.getProduct("Nuts", "Walnut")
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