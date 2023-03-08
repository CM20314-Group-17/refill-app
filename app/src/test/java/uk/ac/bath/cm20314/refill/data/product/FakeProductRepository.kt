package uk.ac.bath.cm20314.refill.data.product

import java.util.UUID

class FakeProductRepository : ProductRepository {

    val data = mutableListOf(
        Product(productId = "1", categoryId = "1", name = "Product 1", pricePerKg = 1, portionSize = 1f, isUpdated = true),
        Product(productId = "2", categoryId = "1", name = "Product 2", pricePerKg = 2, portionSize = 2f, isUpdated = true),
        Product(productId = "3", categoryId = "1", name = "Product 3", pricePerKg = 3, portionSize = 3f, isUpdated = true)
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