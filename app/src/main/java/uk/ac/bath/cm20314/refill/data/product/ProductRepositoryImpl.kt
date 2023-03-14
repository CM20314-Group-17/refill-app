package uk.ac.bath.cm20314.refill.data.product

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.bath.cm20314.refill.data.asFlow

object ProductRepositoryImpl : ProductRepository {

    private val reference = FirebaseDatabase.getInstance().getReference("Categories")

    override fun getAllProducts(): Flow<List<Product>> {
        return reference.asFlow().map { snapshot ->
            snapshot.children.flatMap { category ->
                category.children.mapNotNull { product ->
                    product.getValue(Product::class.java)?.also {
                        it.categoryName = category.key ?: return@mapNotNull null
                        it.productName = product.key ?: return@mapNotNull null
                    }
                }
            }
        }
    }

    override fun getProducts(categoryName: String): Flow<List<Product>> {
        return reference.child(categoryName).asFlow().map { snapshot ->
            snapshot.children.mapNotNull { child ->
                child.getValue(Product::class.java)?.also {
                    it.categoryName = categoryName
                    it.productName = child.key ?: return@mapNotNull null
                }
            }
        }
    }

    override fun getProduct(categoryName: String, productName: String): Flow<Product?> {
        return reference.child(categoryName).child(productName).asFlow().map { snapshot ->
            snapshot.getValue(Product::class.java)?.also {
                it.categoryName = categoryName
                it.productName = productName
            }
        }
    }

    override fun updateProduct(product: Product) {
        // TODO - More difficult because the product name might have changed (see issue #7).
    }

    override fun createProduct(product: Product) {
        val categoryRef = reference.child(product.categoryName)
        val productKey = categoryRef.push().key
        val productRef = categoryRef.child(productKey!!)
        productRef.setValue(product)
        //reference.child(product.categoryName).child(product.productName).setValue(product)
    }

    override fun deleteProduct(categoryName: String, productName: String) {
        reference.child(categoryName).child(productName).removeValue()
    }
}