package uk.ac.bath.cm20314.refill.data.category

val defaultCategoryRepository: CategoryRepository = FakeCategoryRepository

interface CategoryRepository {

    /** Get all categories. */
    suspend fun getCategories(): List<Category>

    /** Gets the category with a particular id. */
    suspend fun getCategory(categoryName: String): Category?

    /** Updates an existing category. */
    suspend fun updateCategory(category: Category, name: String)

    /** Creates a new category. */
    suspend fun createCategory(categoryName: String): Category

    /** Deletes an existing category. */
    suspend fun deleteCategory(categoryName: String)
}