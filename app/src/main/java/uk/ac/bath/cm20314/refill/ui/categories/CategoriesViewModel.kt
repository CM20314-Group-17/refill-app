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
        if (category.categoryName.isBlank()) {
            return
        }
        viewModelScope.launch {
            repository.createCategory(category).also { success ->
                if (!success) {
                    channel.send("Category already exists")
                }
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