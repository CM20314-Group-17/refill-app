package uk.ac.bath.cm20314.refill.data.product

interface ProductRepository {

    /** Gets the products in a particular category. */
    suspend fun getProducts(categoryId: String): List<Product>

    /** Gets the product with a particular id. */
    suspend fun getProduct(productId: String): Product?

    /** Updates an existing product. */
    suspend fun updateProduct(product: Product)

    /** Creates a new product. */
    suspend fun createProduct(name: String, pricePerKg: Int, portionSize: Float): Product
}