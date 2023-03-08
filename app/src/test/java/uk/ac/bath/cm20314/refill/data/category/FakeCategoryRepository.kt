package uk.ac.bath.cm20314.refill.data.category

class FakeCategoryRepository : CategoryRepository {

    var data = mutableListOf(
        Category(categoryName = "Category 1", itemCount = 1, isUpdated = true),
        Category(categoryName = "Category 2", itemCount = 2, isUpdated = true),
        Category(categoryName = "Category 3", itemCount = 3, isUpdated = true)
    )

    override suspend fun getCategories(): List<Category> {
        return data.map { it.copy() }
    }

    override suspend fun getCategory(categoryName: String): Category? {
        return data.find { it.categoryName == categoryName }
    }

    override suspend fun updateCategory(category: Category, name: String) {
        getCategory(category.categoryName)?.apply {
            categoryName = name
            isUpdated = category.isUpdated
        }
    }

    override suspend fun createCategory(categoryName: String): Category {
        return Category(categoryName = categoryName, itemCount = 0).also { data.add(it) }
    }

    override suspend fun deleteCategory(categoryName: String) {
        data.removeIf { it.categoryName == categoryName }
    }
}