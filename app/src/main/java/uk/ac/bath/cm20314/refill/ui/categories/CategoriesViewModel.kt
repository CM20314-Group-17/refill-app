package uk.ac.bath.cm20314.refill.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.defaultCategoryRepository

class CategoriesViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val channel = Channel<String>()

    val messages = channel.receiveAsFlow()
    val categories = repository.getCategories()

    fun createCategory(category: Category) {
        viewModelScope.launch {
            val message = when {
                category.categoryName.isBlank() -> "Category must have a name"
                !repository.createCategory(category) -> "Category already exists"
                else -> null
            }
            message?.let { channel.send(it) }
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