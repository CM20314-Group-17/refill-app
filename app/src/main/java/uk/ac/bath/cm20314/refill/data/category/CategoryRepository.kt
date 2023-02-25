package uk.ac.bath.cm20314.refill.data.category

interface CategoryRepository {

    /** Get all categories. */
    suspend fun getCategories(): List<Category>

    /** Gets the category with a particular id. */
    suspend fun getCategory(categoryId: String): Category?

    /** Updates an existing category. */
    suspend fun updateCategory(category: Category)

    /** Creates a new category. */
    suspend fun createCategory(name: String): Category
}