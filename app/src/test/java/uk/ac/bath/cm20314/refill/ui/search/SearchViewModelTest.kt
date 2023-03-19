package uk.ac.bath.cm20314.refill.ui.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var products: FakeProductRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        products = FakeProductRepository()
        viewModel = SearchViewModel(products)
    }

    @Test
    fun testSearchEmptyQuery() = runTest {
        assertEquals(0, viewModel.results.first().size)
    }

    @Test
    fun testSearchMultipleResults() = runTest {
        viewModel.onQueryChange(query = "p")
        assertEquals(products.data.value, viewModel.results.first())
    }

    @Test
    fun testSearchOneResult() = runTest {
        viewModel.onQueryChange(query = "Product 1")
        assertEquals(1, viewModel.results.first().size)
        assertEquals(products.data.value[0], viewModel.results.first()[0])
    }

    @Test
    fun testSearchNoResults() = runTest {
        viewModel.onQueryChange(query = "Nothing")
        assertEquals(0, viewModel.results.first().size)
    }
}