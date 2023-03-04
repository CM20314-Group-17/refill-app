package uk.ac.bath.cm20314.refill.data.product


object ProductRepositoryImpl : ProductRepository {


    override suspend fun getProducts(categoryId: String): List<Product> {
        // TODO: Replace with products retrieved from the database.
        return listOf(
            Product(id = "test", name = "Spaghetti", pricePerKg = 9, portionSize = 100f),
            Product(id = "test", name = "Pennette (White)", pricePerKg = 8, portionSize = 100f),
            Product(id = "test", name = "Pennette (Wholewheat)", pricePerKg = 9, portionSize = 100f),
            Product(id = "test", name = "Tagliatelle", pricePerKg = 5, portionSize = 100f),
            Product(id = "test", name = "Vermicelli Noodles", pricePerKg = 4, portionSize = 100f)
        )
    }


    override suspend fun getProduct(productId: String): Product? {
        TODO()
    }

    override suspend fun updateProduct(product: Product) {
        TODO()
    }

    override suspend fun createProduct(name: String, pricePerKg: Int, portionSize: Float): Product {
        TODO()
    }

    override suspend fun deleteProduct(productId: String) {
        TODO()
    }
}