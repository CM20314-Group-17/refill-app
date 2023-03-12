package uk.ac.bath.cm20314.refill.data.product

object FakeProductRepository : ProductRepository {

    val data = mutableListOf(
        Product(categoryName = "Category 1", productName = "Product 1", pricePerKg = 1, portionSize = 1f, isUpdated = true),
        Product(categoryName = "Category 1", productName = "Product 2", pricePerKg = 2, portionSize = 2f, isUpdated = true),
        Product(categoryName = "Category 1", productName = "Product 3", pricePerKg = 3, portionSize = 3f, isUpdated = true),
        Product(categoryName = "Category 2", productName = "Product 4", pricePerKg = 4, portionSize = 4f, isUpdated = true),
        Product(categoryName = "Category 2", productName = "Product 5", pricePerKg = 5, portionSize = 5f, isUpdated = true),
        Product(categoryName = "Category 2", productName = "Product 6", pricePerKg = 6, portionSize = 6f, isUpdated = true),
        Product(categoryName = "Category 3", productName = "Product 7", pricePerKg = 7, portionSize = 7f, isUpdated = true),
        Product(categoryName = "Category 3", productName = "Product 8", pricePerKg = 8, portionSize = 8f, isUpdated = true),
        Product(categoryName = "Category 3", productName = "Product 9", pricePerKg = 9, portionSize = 9f, isUpdated = true),
    )

    override suspend fun getProducts(categoryName: String): List<Product> {
        return data.filter { it.categoryName == categoryName }.map { it.copy() }
    }

    override suspend fun getProduct(productName: String, categoryName: String): Product? {
        return data.find { it.categoryName == categoryName && it.productName == productName }
    }

    override suspend fun updateProduct(product: Product) {
        getProduct(product.productName, product.categoryName)?.apply {
            productName = product.productName
            pricePerKg = product.pricePerKg
            portionSize = product.portionSize
            isUpdated = false
        }
    }

    override suspend fun createProduct(
        categoryName: String,
        productName: String,
        pricePerKg: Int,
        portionSize: Float,
        isUpdated: Boolean
    ): Product {
        return Product(categoryName, productName, pricePerKg, portionSize).also { data.add(it) }
    }

    override suspend fun deleteProduct(productName: String, categoryName: String) {
        data.removeIf { it.categoryName == categoryName && it.productName == productName }
    }
}