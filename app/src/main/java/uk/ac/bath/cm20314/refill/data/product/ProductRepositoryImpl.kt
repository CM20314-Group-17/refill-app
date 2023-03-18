package uk.ac.bath.cm20314.refill.data.product

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.flow.Flow
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

    override fun createProduct(product: Product) {// i dont know how flow thing works so ye
        val categoryRef = reference.child(product.categoryId)
        //val productKey = categoryRef.child("products").push().key OLD CODE
        //val productRef = categoryRef.child("products").child(productKey!!) OLD CODE
        val productRef = categoryRef.child("products")

        productRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (productSnapshot in snapshot.children) {
                    val existingProduct = productSnapshot.getValue(Product::class.java)
                    if (existingProduct?.productName == product.productName) {
                        //message here
                        return
                    }
                }
                val productKey = productRef.push().key
                val newProductRef = productRef.child(productKey!!)
                newProductRef.setValue(product)
                //maybe add some message here
            }

            override fun onCancelled(error: DatabaseError) {
                //add some error message here
            }
        })


        //productRef.setValue(product) OLD CODE
        //reference.child(product.categoryName).child(product.productName).setValue(product) OLD CODE
    }

    override fun deleteProduct(categoryId: String, productId: String) {
        reference.child(categoryId).child("products").child(productId).removeValue()
    }
}