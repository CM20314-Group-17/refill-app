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

    enum class Event {
        CategoryCreated
    }

    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val categories = repository.getCategories()

    /** Creates a new category with a particular name. */
    fun createCategory(category: Category) {
        repository.createCategory(category)
        _events.trySend(Event.CategoryCreated)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CategoriesViewModel(defaultCategoryRepository)
            }
        }
    }
}