package uk.ac.bath.cm20314.refill.data.product

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import uk.ac.bath.cm20314.refill.data.asFlow

object ProductRepositoryImpl : ProductRepository {

    private val reference = FirebaseDatabase.getInstance().getReference("Categories")

    override fun getAllProducts(): Flow<List<Product>> {
        return reference.asFlow().map { categories ->
            categories.children.flatMap { category ->
                category.child("products").children.mapNotNull { product ->
                    product.getValue(Product::class.java)?.also {
                        it.categoryId = category.key ?: return@mapNotNull null
                        it.productId = product.key ?: return@mapNotNull null
                    }
                }
            }
        }
    }

    override fun getProducts(categoryId: String): Flow<List<Product>> {
        return reference.child(categoryId).child("products").asFlow().map { snapshot ->
            snapshot.children.mapNotNull { child ->
                child.getValue(Product::class.java)?.also {
                    it.categoryId = categoryId
                    it.productId = child.key ?: return@mapNotNull null
                }
            }
        }
    }

    override fun getProduct(categoryId: String, productId: String): Flow<Product?> {
        return reference.child(categoryId).child("products").child(productId).asFlow().map { product ->
            product.getValue(Product::class.java)?.also {
                it.categoryId = categoryId
                it.productId = productId
            }
        }
    }

    override fun updateProduct(product: Product) {
        reference.child(product.categoryId).child("products").child(product.productId).setValue(product)
    }

    override suspend fun createProduct(product: Product): Boolean {
        val productsRef = reference.child(product.categoryId).child("products")
        val products = getProducts(product.categoryId).first()

        if (products.any { it.productName == product.productName }) {
            return false
        }

        productsRef.push().setValue(product)
        return true
    }

    override fun deleteProduct(categoryId: String, productId: String) {
        reference.child(categoryId).child("products").child(productId).removeValue()
    }
}