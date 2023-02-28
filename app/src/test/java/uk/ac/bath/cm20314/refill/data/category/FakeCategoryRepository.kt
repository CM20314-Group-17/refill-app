package uk.ac.bath.cm20314.refill.data.category

class FakeCategoryRepository : CategoryRepository {

    var data = mutableListOf(
        Category(id = "1", name = "category1", itemCount = 1, isUpdated = true),
        Category(id = "2", name = "category2", itemCount = 2, isUpdated = true),
        Category(id = "3", name = "category3", itemCount = 3, isUpdated = true)
    )

    override suspend fun getCategories(): List<Category> {
        return data.map { it.copy() }
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return data.find { it.id == categoryId }
    }

    override suspend fun updateCategory(category: Category) {
        getCategory(category.id)?.apply { name = category.name }
    }

    override suspend fun createCategory(name: String): Category {
        return Category(id = "4", name = name, itemCount = 0).also { data.add(it) }
    }

    override suspend fun deleteCategory(categoryId: String) {
        data.removeIf { it.id == categoryId }
    }
}