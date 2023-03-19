package uk.ac.bath.cm20314.refill.ui.product

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var products: FakeProductRepository
    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        products = FakeProductRepository()
        viewModel = ProductViewModel("1", "1", products)
    }

    @Test
    fun testLoadProduct() = runTest {
        assertEquals(products.data.value[0], viewModel.product.first())
    }

    @Test
    fun testUpdateProduct() = runTest {
        viewModel.updateProduct(viewModel.product.first()!!.copy(productName = "Updated"))
        assertEquals("Updated", products.data.value[0].productName)
        assertEquals("Updated", viewModel.product.first()?.productName)
    }

    @Test
    fun testDeleteProduct() = runTest {
        viewModel.deleteProduct()
        assertNull(products.data.value.find { it.productName == "Product 1" })
    }
}