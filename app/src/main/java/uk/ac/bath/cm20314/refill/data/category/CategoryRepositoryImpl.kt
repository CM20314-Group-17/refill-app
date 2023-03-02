package uk.ac.bath.cm20314.refill.data.category

import java.util.UUID

object CategoryRepositoryImpl : CategoryRepository {

    private var data = mutableListOf(
        Category(id = "1", name = "Category 1", itemCount = 1, isUpdated = true),
        Category(id = "2", name = "Category 2", itemCount = 2, isUpdated = true),
        Category(id = "3", name = "Category 3", itemCount = 3, isUpdated = true)
    )

    override suspend fun getCategories(): List<Category> {
        return data.map { it.copy() }
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return data.find { it.id == categoryId }
    }

    override suspend fun updateCategory(category: Category) {
        getCategory(category.id)?.apply {
            name = category.name
            isUpdated = category.isUpdated
        }
    }

    override suspend fun createCategory(name: String): Category {
        return Category(UUID.randomUUID().toString(), name, itemCount = 0).also { data.add(it) }
    }

    override suspend fun deleteCategory(categoryId: String) {
        data.removeIf { it.id == categoryId }
    }
}