package uk.ac.bath.cm20314.refill.data.category

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uk.ac.bath.cm20314.refill.data.product.Product

private lateinit var database: DatabaseReference
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
        database = FirebaseDatabase.getInstance().getReference("Categories").child(name)
        database.setValue(name)
        return Category("2", "Nuts", 0, true)
    }

    override suspend fun deleteCategory(categoryId: String) {

    }
}