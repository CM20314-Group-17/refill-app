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

    @Before
    fun setupCoroutines() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun testLoadCategories() = runTest {
        val repository = FakeCategoryRepository()
        val viewModel = CategoriesViewModel(repository)

        viewModel.loadCategories()
        assertArrayEquals(repository.data.toTypedArray(), viewModel.categories.value.toTypedArray())
    }

    @Test
    fun testCreateCategory() = runTest {
        val repository = FakeCategoryRepository()
        val viewModel = CategoriesViewModel(repository)

        viewModel.createCategory("category4")
        assertEquals("category4", repository.data.last().name)
        assertEquals(CategoriesViewModel.Event.CategoryCreated, viewModel.events.first())
    }

    @Test
    fun testReloadCategoriesAfterCreate() = runTest {
        val repository = FakeCategoryRepository()
        val viewModel = CategoriesViewModel(repository)

        viewModel.createCategory("category4")
        viewModel.events.first()
        assertArrayEquals(repository.data.toTypedArray(), viewModel.categories.value.toTypedArray())
    }

    @Test
    fun testUndoCreateCategory() = runTest {
        val repository = FakeCategoryRepository()
        val viewModel = CategoriesViewModel(repository)

        viewModel.createCategory("category4")
        viewModel.events.first()
        viewModel.undoCreateCategory()
        assertNull(repository.data.find { it.name == "category4" })
    }

    @Test
    fun testReloadCategoriesAfterUndo() = runTest {
        val repository = FakeCategoryRepository()
        val viewModel = CategoriesViewModel(repository)

        viewModel.createCategory("category4")
        viewModel.events.first()
        viewModel.undoCreateCategory()
        assertArrayEquals(repository.data.toTypedArray(), viewModel.categories.value.toTypedArray())
    }
}