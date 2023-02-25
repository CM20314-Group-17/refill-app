package uk.ac.bath.cm20314.refill.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.CategoryRepository
import uk.ac.bath.cm20314.refill.data.category.CategoryRepositoryImpl

class CategoriesViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow(listOf<Category>())
    private val _searchText = MutableStateFlow("")

    val categories = _categories.asStateFlow()
    val searchText = _searchText.asStateFlow()

    init {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    fun updateSearchResults(search: String) {
        _searchText.value = search
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CategoriesViewModel(CategoryRepositoryImpl)
            }
        }
    }
}