package uk.ac.bath.cm20314.refill.data.category

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.bath.cm20314.refill.data.asFlow

object CategoryRepositoryImpl : CategoryRepository {

    private val reference = FirebaseDatabase.getInstance().getReference("Categories")

    override fun getCategories(): Flow<List<Category>> {
        return reference.asFlow().map { snapshot ->
            snapshot.children.mapNotNull { child ->
                Category(
                    categoryName = child.key ?: return@mapNotNull null,
                    itemCount = child.childrenCount.toInt(),
                    thumbnail = child.child("thumbnail").getValue(Int::class.java) ?: 0
                )
            }
        }
    }

    override fun getCategory(categoryName: String): Flow<Category?> {
        return reference.child(categoryName).asFlow().map { snapshot ->
            Category(
                categoryName = categoryName,
                itemCount = snapshot.childrenCount.toInt(),
                thumbnail = snapshot.child("thumbnail").getValue(Int::class.java) ?: 0
            )
        }
    }

    override fun updateCategory(category: Category) {
        // TODO - More difficult because the category name might have changed (see issue #10).
    }

    override fun createCategory(category: Category) {
        reference.child(category.categoryName).setValue(true)
    }

    override fun deleteCategory(categoryName: String) {
        reference.child(categoryName).removeValue()
    }
}