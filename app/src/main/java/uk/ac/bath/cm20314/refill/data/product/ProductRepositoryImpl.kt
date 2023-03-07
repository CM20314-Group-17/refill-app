package uk.ac.bath.cm20314.refill.data.product

import java.util.*

object ProductRepositoryImpl : ProductRepository {

    val data = mutableListOf(
        Product(productId = "1", categoryId = "1", name = "Product 1", pricePerKg = 1, portionSize = 1f, isUpdated = true),
        Product(productId = "2", categoryId = "1", name = "Product 2", pricePerKg = 2, portionSize = 2f, isUpdated = true),
        Product(productId = "3", categoryId = "1", name = "Product 3", pricePerKg = 3, portionSize = 3f, isUpdated = true),
        Product(productId = "4", categoryId = "2", name = "Product 4", pricePerKg = 4, portionSize = 4f, isUpdated = true),
        Product(productId = "5", categoryId = "2", name = "Product 5", pricePerKg = 5, portionSize = 5f, isUpdated = true),
        Product(productId = "6", categoryId = "2", name = "Product 6", pricePerKg = 6, portionSize = 6f, isUpdated = true),
        Product(productId = "7", categoryId = "3", name = "Product 7", pricePerKg = 7, portionSize = 7f, isUpdated = true),
        Product(productId = "8", categoryId = "3", name = "Product 8", pricePerKg = 8, portionSize = 8f, isUpdated = true),
        Product(productId = "9", categoryId = "3", name = "Product 9", pricePerKg = 9, portionSize = 9f, isUpdated = true)
    )

    override suspend fun getProducts(categoryId: String): List<Product> {
        return data.filter { it.categoryId == categoryId }.map { it.copy() }
    }

    override suspend fun getProduct(categoryId: String, productId: String): Product? {
        return data.find { it.productId == productId }
    }

    override suspend fun updateProduct(product: Product) {
        getProduct(product.categoryId, product.productId)?.apply {
            name = product.name
            pricePerKg = product.pricePerKg
            portionSize = product.portionSize
            isUpdated = product.isUpdated
        }
    }

    override suspend fun createProduct(categoryId: String, name: String, pricePerKg: Int, portionSize: Float): Product {
        return Product(categoryId, UUID.randomUUID().toString(), name, pricePerKg, portionSize).also { data.add(it) }
    }

    override suspend fun deleteProduct(categoryId: String, productId: String) {
        data.removeIf { it.categoryId == categoryId && it.productId == productId }
    }
}