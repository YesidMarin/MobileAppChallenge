package com.yesidmarin.mobileappchallenge

import app.cash.turbine.test
import com.yesidmarin.mobileappchallenge.domain.SearchProductsUseCase
import com.yesidmarin.mobileappchallenge.helpers.CoroutineRule
import com.yesidmarin.mobileappchallenge.helpers.dummyProducts
import com.yesidmarin.mobileappchallenge.ui.viewmodel.ResultViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResultViewModelTest {

    @get:Rule val coroutineRule = CoroutineRule()

    private val searchUC: SearchProductsUseCase = mockk()
    private lateinit var vm: ResultViewModel

    @Before fun setUp() {
        vm = ResultViewModel(searchUC)
    }

    @Test
    fun `searchProducts success fills productsResult and total`() = runTest(coroutineRule.dispatcher) {
        val page1 = dummyProducts(total = 3)
        coEvery { searchUC("MCO","active",null,"iphone",10,0) } returns
                Result.success(page1)

        vm.searchProducts(
            query  = "iphone",
            siteId = "MCO",
            status = "active",
            offset = 0,
            limit  = 10,
            productIdentifier = false
        )

        vm.productsResult.test {
            assertNull(awaitItem())
            assertEquals(page1, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        vm.total.test {
            assertEquals(3, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            searchUC("MCO","active",null,"iphone",10,0)
        }
        assertFalse(vm.isLoadingMore.value)
    }


    @Test
    fun `searchProducts empty results sets errorState`() = runTest(coroutineRule.dispatcher) {
        val empty = dummyProducts(total = 0, items = emptyList())
        coEvery { searchUC(any(), any(), any(), any(), any(), any()) } returns
                Result.success(empty)

        vm.searchProducts("iphone","MCO","active",0,10,false)

        vm.errorState.test {
            assertNull(awaitItem())
            assertEquals(
                "No se encontraron productos para la búsqueda: iphone",
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        assertNull(vm.productsResult.value)
    }


    @Test
    fun `searchProducts failure sets errorState`() = runTest(coroutineRule.dispatcher) {
        coEvery { searchUC(any(), any(), any(), any(), any(), any()) } returns
                Result.failure(Exception("Boom"))

        vm.searchProducts("iphone","MCO","active",0,10,false)

        vm.errorState.test {
            assertNull(awaitItem())
            assertEquals("Boom", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `navigateTo sets and clears navigationEvent`() = runTest(coroutineRule.dispatcher) {
        vm.navigationEvent.test {
            assertNull(awaitItem())

            vm.navigateTo("results_screen")
            assertEquals("results_screen", awaitItem())

            vm.clearNavigationEvent()
            assertNull(awaitItem())

            expectNoEvents()
            cancelAndConsumeRemainingEvents()
        }
    }
}
