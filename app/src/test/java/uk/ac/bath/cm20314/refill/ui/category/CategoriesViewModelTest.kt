package uk.ac.bath.cm20314.refill.ui.category

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.category.FakeCategoryRepository
import uk.ac.bath.cm20314.refill.ui.categories.CategoriesViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    private lateinit var repository: FakeCategoryRepository
    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = FakeCategoryRepository()
        viewModel = CategoriesViewModel(repository)
    }

    @Test
    fun testLoadCategories() = runTest {
        viewModel.loadCategories()
        assertArrayEquals(repository.data.toTypedArray(), viewModel.categories.value.toTypedArray())
    }

    @Test
    fun testCreateCategory() = runTest {
        viewModel.createCategory("Category 4")
        assertEquals("Category 4", repository.data.last().name)
        assertEquals(CategoriesViewModel.Event.CategoryCreated, viewModel.events.first())
    }

    @Test
    fun testReloadCategoriesAfterCreate() = runTest {
        viewModel.createCategory("Category 4")
        viewModel.events.first()
        assertArrayEquals(repository.data.toTypedArray(), viewModel.categories.value.toTypedArray())
    }

    @Test
    fun testUndoCreateCategory() = runTest {
        viewModel.createCategory("Category 4")
        viewModel.events.first()
        viewModel.undoCreateCategory()
        assertNull(repository.data.find { it.name == "Category 4" })
    }

    @Test
    fun testReloadCategoriesAfterUndo() = runTest {
        viewModel.createCategory("Category 4")
        viewModel.events.first()
        viewModel.undoCreateCategory()
        assertArrayEquals(repository.data.toTypedArray(), viewModel.categories.value.toTypedArray())
    }
}