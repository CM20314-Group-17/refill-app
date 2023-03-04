package uk.ac.bath.cm20314.refill.data.product

import java.util.*

object ProductRepositoryImpl : ProductRepository {

    val data = mutableListOf(
        Product(id = "1", name = "Product 1", pricePerKg = 1, portionSize = 1f, isUpdated = true),
        Product(id = "2", name = "Product 2", pricePerKg = 2, portionSize = 2f, isUpdated = true),
        Product(id = "3", name = "Product 3", pricePerKg = 3, portionSize = 3f, isUpdated = true)
    )

    override suspend fun getProducts(categoryId: String): List<Product> {
        return if (categoryId == "1") data.map { it.copy() } else emptyList()
    }

    override suspend fun getProduct(productId: String): Product? {
        return data.find { it.id == productId }
    }

    override suspend fun updateProduct(product: Product) {
        getProduct(product.id)?.apply {
            name = product.name
            pricePerKg = product.pricePerKg
            portionSize = product.portionSize
            isUpdated = product.isUpdated
        }
    }

    override suspend fun createProduct(name: String, pricePerKg: Int, portionSize: Float): Product {
        return Product(UUID.randomUUID().toString(), name, pricePerKg, portionSize).also { data.add(it) }
    }

    override suspend fun deleteProduct(productId: String) {
        data.removeIf { it.id == productId }
    }
}