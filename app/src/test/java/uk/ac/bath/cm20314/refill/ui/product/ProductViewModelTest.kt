package uk.ac.bath.cm20314.refill.ui.product

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var productRepository: FakeProductRepository
    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        productRepository = FakeProductRepository()
        viewModel = ProductViewModel("Category 1", "Product 1", productRepository)
    }

    @Test
    fun testLoadProduct() = runTest {
        assertEquals(productRepository.data.value[0], viewModel.product.first())
    }
}