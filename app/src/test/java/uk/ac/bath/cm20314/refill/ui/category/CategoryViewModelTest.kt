package uk.ac.bath.cm20314.refill.ui.category

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.FakeCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository
import uk.ac.bath.cm20314.refill.data.product.Product

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModelTest {

    private lateinit var categories: FakeCategoryRepository
    private lateinit var products: FakeProductRepository
    private lateinit var viewModel: CategoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        categories = FakeCategoryRepository()
        products = FakeProductRepository()
        viewModel = CategoryViewModel("1", categories, products)
    }

    @Test
    fun testLoadCategory() = runTest {
        assertEquals(categories.data.value[0], viewModel.category.first())
    }

    @Test
    fun testLoadProducts() = runTest {
        assertEquals(products.data.value, viewModel.products.first())
    }

    @Test
    fun testUpdateCategory() = runTest {
        viewModel.updateCategory(viewModel.category.first()!!.copy(categoryName = "Updated"))
        assertEquals("Updated", categories.data.value[0].categoryName)
        assertEquals("Updated", viewModel.category.first()?.categoryName)
    }

    @Test
    fun testDeleteCategory() = runTest {
        viewModel.deleteCategory()
        assertNull(categories.data.value.find { it.categoryName == "Category 1" })
    }

    @Test
    fun testCreateProduct() = runTest {
        viewModel.createProduct(Product(viewModel.category.first()!!.categoryId, productName = "Product 4"))
        assertEquals("Product 4", products.data.value.last().productName)
        assertEquals("Product 4", viewModel.products.first().last().productName)
    }

    @Test
    fun testCreateProductEmpty() = runTest {
        viewModel.createProduct(Product())
        assertEquals("Product must have a name", viewModel.messages.first())
        assertEquals(3, products.data.value.size)
        assertEquals(3, viewModel.products.first().size)
    }

    @Test
    fun testCreateProductDuplicate() = runTest {
        viewModel.createProduct(Product(categoryId = "1", productName = "Product 1"))
        assertEquals("Product already exists", viewModel.messages.first())
        assertEquals(3, products.data.value.size)
        assertEquals(3, viewModel.products.first().size)
    }

    @Test
    fun testCreateProductDuplicateDifferentCategory() = runTest {
        viewModel.createProduct(Product(categoryId = "2", productName = "Product 1"))
        assertEquals(4, products.data.value.size)
    }
}