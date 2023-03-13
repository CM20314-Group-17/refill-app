package uk.ac.bath.cm20314.refill.data.category

import android.content.ContentValues
import android.util.Log
import com.google.firebase.database.*

private lateinit var database: DatabaseReference
object CategoryRepositoryImpl : CategoryRepository {


    override suspend fun getCategories(): List<Category> {
        // TODO: Replace with categories retrieved from the database.
        return listOf(
            Category(categoryName = "Dried Fruits", itemCount = 9),
            Category(categoryName = "Pasta", itemCount = 8),
            Category(categoryName = "Nuts & Seeds", itemCount = 9),
            Category(categoryName = "Rice", itemCount = 5),
            Category(categoryName = "Beans", itemCount = 4)
        )
    }

    override suspend fun getCategory(categoryName: String): Category? {
        TODO()
    }

    override suspend fun updateCategory(category: Category, name: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Categories")
        ref.child(category.categoryName).setValue(name)
    }

    override suspend fun createCategory(categoryName: String): Category {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Categories")
        val category = Category(categoryName = categoryName, itemCount = 0, isUpdated = true)
        val newRef = ref.child(categoryName)
        newRef.setValue(category)
        return category
    }

    override suspend fun deleteCategory(categoryName: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Categories")
        val query = ref.orderByKey().equalTo(categoryName)

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
