package uk.ac.bath.cm20314.refill.data.category

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import uk.ac.bath.cm20314.refill.data.asFlow

object CategoryRepositoryImpl : CategoryRepository {

    private val reference = FirebaseDatabase.getInstance().getReference("Categories")

    override fun getCategories(): Flow<List<Category>> {
        return reference.asFlow().map { categories ->
            categories.children.mapNotNull { category ->
                Category(
                    categoryId = category.key ?: return@mapNotNull null,
                    categoryName = category.child("categoryName").value.toString(),
                    itemCount = category.child("products").childrenCount.toInt(),
                    thumbnail = category.child("thumbnail").getValue(Int::class.java) ?: 0
                )
            }
        }
    }

    override fun getCategory(categoryId: String): Flow<Category?> {
        return reference.child(categoryId).asFlow().map { category ->
            Category(
                categoryId = category.key ?: return@map null,
                categoryName = category.child("categoryName").value.toString(),
                itemCount = category.child("products").childrenCount.toInt(),
                thumbnail = category.child("thumbnail").getValue(Int::class.java) ?: 0
            )
        }
    }

    override fun updateCategory(category: Category) {
        val categoryReference = reference.child(category.categoryId)
        categoryReference.child("categoryName").setValue(category.categoryName)
        categoryReference.child("thumbnail").setValue(category.thumbnail)
    }

    override suspend fun createCategory(category: Category): Boolean {
        val categoryRef = reference.child(category.categoryId)
        val categories = getCategories().first()

        if (categories.any { it.categoryName == category.categoryName }) {
            return false
        }

        categoryRef.push().setValue(category)
        return true
    }

    override fun deleteCategory(categoryId: String) {
        reference.child(categoryId).removeValue()
    }
}