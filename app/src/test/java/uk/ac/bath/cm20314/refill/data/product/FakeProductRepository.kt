package uk.ac.bath.cm20314.refill.data.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.*

class FakeProductRepository : ProductRepository {

    val data = MutableStateFlow(
        value = listOf(
            Product(categoryId = "1", productId = "1", productName = "Product 1", pricePerKg = 1, portionSize = 1f),
            Product(categoryId = "1", productId = "2", productName = "Product 2", pricePerKg = 2, portionSize = 2f),
            Product(categoryId = "1", productId = "3", productName = "Product 3", pricePerKg = 3, portionSize = 3f),
        )
    )

    override fun getAllProducts(): Flow<List<Product>> {
        return data
    }

    override fun getProducts(categoryId: String): Flow<List<Product>> {
        return data.map { products ->
            products.filter { it.categoryId == categoryId }
        }
    }

    override fun getProduct(categoryId: String, productId: String): Flow<Product?> {
        return data.map { products ->
            products.find { it.categoryId == categoryId && it.productId == productId }
        }
    }

    override fun updateProduct(product: Product) {
        product.isUpdated = false
        data.value = data.value.map {
            if (it.categoryId == product.categoryId && it.productId == product.productId) product else it
        }
    }

    override fun createProduct(product: Product) {
        data.value = data.value.toMutableList().apply {
            add(product.copy(productId = UUID.randomUUID().toString()))
        }
    }

    override fun deleteProduct(categoryId: String, productId: String) {
        data.value = data.value.mapNotNull {
            if (it.categoryId == categoryId && it.productId == productId) null else it
        }
    }
}