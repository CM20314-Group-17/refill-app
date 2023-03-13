package uk.ac.bath.cm20314.refill.data.product

import kotlinx.coroutines.flow.Flow

var defaultProductRepository: ProductRepository = ProductRepositoryImpl

interface ProductRepository {

    /** Gets all products from all categories. */
    fun getAllProducts(): Flow<List<Product>>

    /** Gets the products in a particular category. */
    fun getProducts(categoryName: String): Flow<List<Product>>

    /** Gets the product in a particular category with a particular id. */
    fun getProduct(categoryName: String, productName: String): Flow<Product?>

    /** Updates an existing product. */
    fun updateProduct(product: Product)

    /** Creates a new product. */
    fun createProduct(product: Product)

    /** Deletes an existing product. */
    fun deleteProduct(categoryName: String, productName: String)
}