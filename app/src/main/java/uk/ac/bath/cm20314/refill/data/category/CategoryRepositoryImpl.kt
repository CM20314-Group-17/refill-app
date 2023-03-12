package uk.ac.bath.cm20314.refill.data.category

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: DatabaseReference
object CategoryRepositoryImpl : CategoryRepository {


    override suspend fun getCategories(): List<Category> {
        // TODO: Replace with categories retrieved from the database.
        return listOf(
            Category(categoryName = "test", isUpdated = true, itemCount = 9),
            Category(categoryName = "test", isUpdated = true, itemCount = 8),
            Category(categoryName = "test", isUpdated = true, itemCount = 9),
            Category(categoryName = "test", isUpdated = true, itemCount = 5),
            Category(categoryName = "test", isUpdated = true, itemCount = 4)
        )
    }

    override suspend fun getCategory(categoryName: String): Category? {
        TODO()
    }

    override suspend fun updateCategory(categoryName: Category, name: String) {
        TODO()
    }

    override suspend fun createCategory(categoryName: String): Category {
        TODO()
    }

    override suspend fun deleteCategory(categoryId: String) {

    }
}