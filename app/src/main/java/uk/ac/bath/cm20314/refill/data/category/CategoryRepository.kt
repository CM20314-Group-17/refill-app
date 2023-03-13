package uk.ac.bath.cm20314.refill.data.category

import kotlinx.coroutines.flow.Flow

var defaultCategoryRepository: CategoryRepository = CategoryRepositoryImpl

interface CategoryRepository {

    /** Get all categories. */
    fun getCategories(): Flow<List<Category>>

    /** Gets the category with a particular id. */
    fun getCategory(categoryName: String): Flow<Category?>

    /** Updates an existing category. */
    fun updateCategory(category: Category)

    /** Creates a new category. */
    fun createCategory(category: Category)

    /** Deletes an existing category. */
    fun deleteCategory(categoryName: String)
}