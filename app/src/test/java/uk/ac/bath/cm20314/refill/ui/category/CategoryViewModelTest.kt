package uk.ac.bath.cm20314.refill.ui.category

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.FakeCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository
import uk.ac.bath.cm20314.refill.data.product.Product

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModelTest {

    private lateinit var categoryRepository: FakeCategoryRepository
    private lateinit var productRepository: FakeProductRepository
    private lateinit var viewModel: CategoryViewModel

    @Before
    fun setup() {
        categoryRepository = FakeCategoryRepository()
        productRepository = FakeProductRepository()
        viewModel = CategoryViewModel("Category 1", categoryRepository, productRepository)
    }

    @Test
    fun testLoadCategory() = runTest {
        assertEquals(categoryRepository.data.value[0], viewModel.category.first())
    }

    @Test
    fun testLoadProducts() = runTest {
        assertEquals(productRepository.data.value, viewModel.products.first())
    }

    @Test
    fun testUpdateCategory() = runTest {
        viewModel.updateCategory(Category(categoryName = "Updated"))
        assertEquals("Updated", categoryRepository.data.value[0].categoryName)
        assertEquals("Updated", viewModel.category.first()?.categoryName)
    }

    @Test
    fun testDeleteCategory() = runTest {
        viewModel.deleteCategory()
        assertNull(categoryRepository.data.value.find { it.categoryName == "Category 1" })
    }

    @Test
    fun testCreateProduct() = runTest {
        viewModel.createProduct(Product(categoryName = "Category 1", productName = "Product 4"))
        assertEquals("Product 4", productRepository.data.value.last().productName)
        assertEquals("Product 4", viewModel.products.first().last().productName)
    }
}