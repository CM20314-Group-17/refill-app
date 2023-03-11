package uk.ac.bath.cm20314.refill.data.product

var defaultProductRepository: ProductRepository = FakeProductRepository

interface ProductRepository {

    /** Gets the products in a particular category. */
    suspend fun getProducts(categoryName: String): List<Product>

    /** Gets the product with a particular id. */
    suspend fun getProduct(productName: String, categoryName: String): Product?

    /** Updates an existing product. */
    suspend fun updateProduct(product: Product)

    /** Creates a new product. */
    suspend fun createProduct(categoryName: String, productName: String, pricePerKg: Int, portionSize: Float, isUpdated: Boolean): Product?

    /** Deletes an existing product. */
    suspend fun deleteProduct(productName: String, categoryName: String)
}