package uk.ac.bath.cm20314.refill.data.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.*

class FakeCategoryRepository : CategoryRepository {

    val data = MutableStateFlow(
        value = listOf(
            Category(categoryName = "Category 1", itemCount = 1),
            Category(categoryName = "Category 2", itemCount = 2),
            Category(categoryName = "Category 3", itemCount = 3),
        )
    )

    override fun getCategories(): Flow<List<Category>> {
        return data
    }

    override fun getCategory(categoryName: String): Flow<Category?> {
        return data.map { categories ->
            categories.find { it.categoryName == categoryName }
        }
    }

    override fun updateCategory(category: Category) {
        data.value = data.value.map {
            if (it.categoryName == category.categoryName) category else it
        }
    }

    override fun createCategory(category: Category) {
        data.value = data.value.toMutableList().apply { add(category) }
    }

    override fun deleteCategory(categoryName: String) {
        data.value = data.value.mapNotNull {
            if (it.categoryName == categoryName) null else it
        }
    }
}