package uk.ac.bath.cm20314.refill.ui.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import uk.ac.bath.cm20314.refill.data.product.FakeProductRepository

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var productRepository: FakeProductRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        productRepository = FakeProductRepository()
        viewModel = SearchViewModel(productRepository)
    }

    @Test
    fun testSearchEmptyQuery() = runTest {
        assertEquals(0, viewModel.results.first().size)
    }

    @Test
    fun testSearchMultipleResults() = runTest {
        viewModel.onQueryChange(query = "p")
        assertEquals(productRepository.data.value, viewModel.results.first())
    }

    @Test
    fun testSearchOneResult() = runTest {
        viewModel.onQueryChange(query = "Product 1")
        assertEquals(1, viewModel.results.first().size)
        assertEquals(productRepository.data.value[0], viewModel.results.first()[0])
    }

    @Test
    fun testSearchNoResults() = runTest {
        viewModel.onQueryChange(query = "Nothing")
        assertEquals(0, viewModel.results.first().size)
    }
}