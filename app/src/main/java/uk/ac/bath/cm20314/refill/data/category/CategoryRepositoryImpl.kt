package uk.ac.bath.cm20314.refill.data.category

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.*

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
        TODO()
    }

    override suspend fun deleteCategory(categoryId: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Categories")
        val query = myRef.orderByKey().equalTo(categoryId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (categorySnapshot in dataSnapshot.children) {
                    categorySnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(ContentValues.TAG, "Error", databaseError.toException())
            }
        })
    }
    }
