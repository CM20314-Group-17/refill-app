package uk.ac.bath.cm20314.refill.data.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.*

object FakeProductRepository : ProductRepository {

    val data = MutableStateFlow(
        value = listOf(
            Product(categoryName = "Category 1", productName = "Product 1", pricePerKg = 1, portionSize = 1f),
            Product(categoryName = "Category 1", productName = "Product 2", pricePerKg = 2, portionSize = 2f),
            Product(categoryName = "Category 1", productName = "Product 3", pricePerKg = 3, portionSize = 3f),
        )
    )

    override fun getAllProducts(): Flow<List<Product>> {
        return data
    }

    override fun getProducts(categoryName: String): Flow<List<Product>> {
        return data.map { products ->
            products.filter { it.categoryName == categoryName }
        }
    }

    override fun getProduct(categoryName: String, productName: String): Flow<Product?> {
        return data.map { products ->
            products.find { it.categoryName == categoryName && it.productName == productName }
        }
    }

    override fun updateProduct(product: Product) {
        product.isUpdated = false
        data.value = data.value.map {
            if (it.categoryName == product.categoryName && it.productName == product.productName) product else it
        }
    }

    override fun createProduct(product: Product) {
        data.value = data.value.toMutableList().apply { add(product) }
    }

    override fun deleteProduct(categoryName: String, productName: String) {
        data.value = data.value.mapNotNull {
            if (it.categoryName == categoryName && it.productName == productName) null else it
        }
    }
}