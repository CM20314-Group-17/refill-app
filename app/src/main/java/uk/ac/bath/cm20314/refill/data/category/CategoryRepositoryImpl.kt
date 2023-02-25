package uk.ac.bath.cm20314.refill.data.category

object CategoryRepositoryImpl : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        // TODO: Replace with categories retrieved from the database.
        return listOf(
            Category(id = "test", name = "Dried Fruits", itemCount = 9),
            Category(id = "test", name = "Pasta", itemCount = 8),
            Category(id = "test", name = "Nuts & Seeds", itemCount = 9),
            Category(id = "test", name = "Rice", itemCount = 5),
            Category(id = "test", name = "Beans", itemCount = 4)
        )
    }

    override suspend fun getCategory(categoryId: String): Category? {
        TODO()
    }

    override suspend fun updateCategory(category: Category) {
        TODO()
    }

    override suspend fun createCategory(name: String): Category {
        TODO()
    }
}