package uk.ac.bath.cm20314.refill.data.category

import java.util.UUID

object CategoryRepositoryImpl : CategoryRepository {

    var data = mutableListOf(
        Category(categoryId = "1", name = "Category 1", itemCount = 1, isUpdated = true),
        Category(categoryId = "2", name = "Category 2", itemCount = 2, isUpdated = true),
        Category(categoryId = "3", name = "Category 3", itemCount = 3, isUpdated = true),
        Category(categoryId = "4", name = "Category 4", itemCount = 4, isUpdated = true),
        Category(categoryId = "5", name = "Category 5", itemCount = 5, isUpdated = true),
        Category(categoryId = "6", name = "Category 6", itemCount = 6, isUpdated = true)
    )

    override suspend fun getCategories(): List<Category> {
        return data.map { it.copy() }
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return data.find { it.categoryId == categoryId }
    }

    override suspend fun updateCategory(category: Category) {
        getCategory(category.categoryId)?.apply {
            name = category.name
            isUpdated = category.isUpdated
        }
    }

    override suspend fun createCategory(name: String): Category {
        return Category(categoryId = "4", name = name, itemCount = 0).also { data.add(it) }
    }

    override suspend fun deleteCategory(categoryId: String) {
        data.removeIf { it.categoryId == categoryId }
    }
}