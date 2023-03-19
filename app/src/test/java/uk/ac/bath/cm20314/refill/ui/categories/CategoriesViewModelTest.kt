package uk.ac.bath.cm20314.refill.ui.categories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.FakeCategoryRepository

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    private lateinit var categories: FakeCategoryRepository
    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        categories = FakeCategoryRepository()
        viewModel = CategoriesViewModel(categories)
    }

    @Test
    fun testLoadCategories() = runTest {
        assertEquals(categories.data.value, viewModel.categories.first())
    }

    @Test
    fun testCreateCategory() = runTest {
        viewModel.createCategory(Category(categoryName = "Category 4"))
        assertEquals("Category 4", categories.data.value.last().categoryName)
        assertEquals("Category 4", viewModel.categories.first().last().categoryName)
    }

    @Test
    fun testCreateCategoryEmpty() = runTest {
        viewModel.createCategory(Category())
        assertEquals("Category must have a name", viewModel.messages.first())
        assertEquals(3, categories.data.value.size)
        assertEquals(3, viewModel.categories.first().size)
    }

    @Test
    fun testCreateCategoryDuplicate() = runTest {
        viewModel.createCategory(Category(categoryName = "Category 1"))
        assertEquals("Category already exists", viewModel.messages.first())
        assertEquals(3, categories.data.value.size)
        assertEquals(3, viewModel.categories.first().size)
    }
}