package uk.ac.bath.cm20314.refill.data.product

import com.google.android.gms.common.api.Response
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

object ProductRepositoryImpl : ProductRepository {

    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val productRef: DatabaseReference = rootRef.child("ProductRef")

    data class Response(
        var products: List<Product>? = null,
        var exception: Exception? = null
    )

    interface FirebaseCallback {
        fun onResponse(response: Response)
    }

    override suspend fun getProducts(categoryId: String): List<Product> {
        // TODO: Replace with products retrieved from the database.
        return listOf(
            Product(id = "test", name = "Spaghetti", pricePerKg = 9, portionSize = 100f),
            Product(id = "test", name = "Pennette (White)", pricePerKg = 8, portionSize = 100f),
            Product(id = "test", name = "Pennette (Wholewheat)", pricePerKg = 9, portionSize = 100f),
            Product(id = "test", name = "Tagliatelle", pricePerKg = 5, portionSize = 100f),
            Product(id = "test", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
        )
    }

    fun getDBResponse(callback: FirebaseCallback) {
        productRef.get().addOnCompleteListener {task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.products = result.children.map {snapShot ->
                        snapShot.getValue(Product::class.java)!!
                    }
                }
            }
            else {
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }

    override suspend fun getProduct(productId: String): Product? {
        TODO()
    }

    override suspend fun updateProduct(product: Product) {
        TODO()
    }

    override suspend fun createProduct(name: String, pricePerKg: Int, portionSize: Float): Product {
        TODO()
    }

    override suspend fun deleteProduct(productId: String) {
        TODO()
    }
}