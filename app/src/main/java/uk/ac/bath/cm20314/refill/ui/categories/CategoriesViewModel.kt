package uk.ac.bath.cm20314.refill.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.defaultCategoryRepository

class CategoriesViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    val categories = repository.getCategories()

    fun createCategory(category: Category) {
        if (category.categoryName.isBlank()) {
            return
        }
        repository.createCategory(category)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CategoriesViewModel(defaultCategoryRepository)
            }
        }
    }
}