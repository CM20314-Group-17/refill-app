package uk.ac.bath.cm20314.refill.ui.category

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import uk.ac.bath.cm20314.refill.data.category.FakeCategoryRepository
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModelTest {

    private lateinit var categoryRepository: FakeCategoryRepository
    private lateinit var productRepository: FakeProductRepository
    private lateinit var viewModel: CategoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        categoryRepository = FakeCategoryRepository()
        productRepository = FakeProductRepository()
        viewModel = CategoryViewModel(categoryId = "1", categoryRepository, productRepository)
    }

    @Test
    fun testLoadCategory() = runTest {
        assertEquals(categoryRepository.data[0], viewModel.category.value)
    }

    @Test
    fun testLoadProducts() = runTest {
        assertArrayEquals(productRepository.data.toTypedArray(), viewModel.products.value.toTypedArray())
    }

    @Test
    fun testUpdateCategory() = runTest {
        viewModel.updateCategory("updated category")
        assertEquals("updated category", categoryRepository.data[0].name)
        assertEquals("updated category", viewModel.category.value?.name)
        assertEquals(CategoryViewModel.Event.CategoryUpdated, viewModel.events.first())
    }

    @Test
    fun testUndoUpdateCategory() = runTest {
        viewModel.updateCategory("updated category")
        viewModel.events.first()
        viewModel.undoUpdateCategory()
        assertEquals("Category 1", categoryRepository.data[0].name)
        assertEquals("Category 1", viewModel.category.value?.name)
    }
}