package uk.ac.bath.cm20314.refill.data.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.*

object FakeCategoryRepository : CategoryRepository {

    val data = MutableStateFlow(
        value = listOf(
            Category(categoryId = "1", categoryName = "Category 1", itemCount = 1),
            Category(categoryId = "2", categoryName = "Category 2", itemCount = 2),
            Category(categoryId = "3", categoryName = "Category 3", itemCount = 3),
        )
    )

    override fun getCategories(): Flow<List<Category>> {
        return data
    }

    override fun getCategory(categoryId: String): Flow<Category?> {
        return data.map { categories ->
            categories.find { it.categoryId == categoryId }
        }
    }

    override fun updateCategory(category: Category) {
        data.value = data.value.map {
            if (it.categoryId == category.categoryId) category else it
        }
    }

    override suspend fun createCategory(category: Category): Boolean {
        if (data.value.any { it.categoryName == category.categoryName }) {
            return false
        }
        data.value = data.value.toMutableList().apply {
            add(category.copy(categoryId = UUID.randomUUID().toString()))
        }
        return true
    }

    override fun deleteCategory(categoryId: String) {
        data.value = data.value.mapNotNull {
            if (it.categoryId == categoryId) null else it
        }
    }
}