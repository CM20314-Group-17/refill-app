package uk.ac.bath.cm20314.refill.data.product

import kotlinx.coroutines.flow.Flow

var defaultProductRepository: ProductRepository = ProductRepositoryImpl

interface ProductRepository {

    /** Gets all products from all categories. */
    fun getAllProducts(): Flow<List<Product>>

    /** Gets the products in a particular category. */
    fun getProducts(categoryId: String): Flow<List<Product>>

    /** Gets the product in a particular category with a particular id. */
    fun getProduct(categoryId: String, productId: String): Flow<Product?>

    /** Updates an existing product. */
    fun updateProduct(product: Product)

    /** Creates a new product. */
    suspend fun createProduct(product: Product): Boolean

    /** Deletes an existing product. */
    fun deleteProduct(categoryId: String, productId: String)
}