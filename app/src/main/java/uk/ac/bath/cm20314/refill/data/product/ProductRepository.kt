package uk.ac.bath.cm20314.refill.data.product

interface ProductRepository {

    /** Gets the products in a particular category. */
    suspend fun getProducts(categoryId: String): List<Product>

    /** Gets the product with a particular id. */
    suspend fun getProduct(categoryId: String, productId: String): Product?

    /** Updates an existing product. */
    suspend fun updateProduct(product: Product)

    /** Creates a new product in a particular category. */
    suspend fun createProduct(categoryId: String, name: String, pricePerKg: Int, portionSize: Float): Product

    /** Deletes an existing product. */
    suspend fun deleteProduct(categoryId: String, productId: String)
}