package uk.ac.bath.cm20314.refill.ui.categories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.category.FakeCategoryRepository

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    private lateinit var repository: FakeCategoryRepository
    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setup() {
        repository = FakeCategoryRepository()
        viewModel = CategoriesViewModel(repository)
    }

    @Test
    fun testLoadCategories() = runTest {
        assertEquals(repository.data.value, viewModel.categories.first())
    }

    @Test
    fun testCreateCategory() = runTest {
        viewModel.createCategory(Category(categoryName = "Category 4"))
        assertEquals("Category 4", repository.data.value.last().categoryName)
        assertEquals("Category 4", viewModel.categories.first().last().categoryName)
    }

    @Test
    fun testCategoryThumbnail() = runTest {
        viewModel.createCategory(Category(thumbnail = 11))
        assertEquals(11, repository.data.value.last().thumbnail)
        assertEquals(11, viewModel.categories.first().last().thumbnail)
    }
}