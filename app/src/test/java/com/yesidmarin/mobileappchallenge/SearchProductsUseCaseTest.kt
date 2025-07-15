package com.yesidmarin.mobileappchallenge

import com.yesidmarin.mobileappchallenge.data.ProductRepository
import com.yesidmarin.mobileappchallenge.domain.SearchProductsUseCase
import com.yesidmarin.mobileappchallenge.helpers.CoroutineRule
import com.yesidmarin.mobileappchallenge.helpers.dummyProducts
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchProductsUseCaseTest {

    @get:Rule
    private val coroutineRule = CoroutineRule()

    private val repo: ProductRepository = mockk()
    private val useCase = SearchProductsUseCase(repo)

    @Test
    fun `invoke returns success from repository`() = runTest(coroutineRule.dispatcher) {
        val expected = Result.success(dummyProducts(total = 2))

        coEvery {
            repo.searchProducts(
                siteId  = "MCO",
                status  = "active",
                productIdentifier = null,
                query   = "iphone",
                limit   = 10,
                offset  = 0
            )
        } returns expected

        val res = useCase(
            siteId = "MCO",
            status = "active",
            query  = "iphone",
            limit  = 10,
            offset = 0
        )

        assertTrue(res.isSuccess)
        assertEquals(expected.getOrNull(), res.getOrNull())

        coVerify(exactly = 1) {           // se delegó una sola vez
            repo.searchProducts("MCO", "active", null, "iphone", 10, 0)
        }
    }

    @Test
    fun `invoke propagates failure from repository`() = runTest(coroutineRule.dispatcher) {
        val failure = Result.failure<Nothing>(IllegalStateException("Boom"))
        coEvery { repo.searchProducts(any(), any(), any(), any(), any(), any()) } returns failure

        val res = useCase("MCO", "active")

        assertTrue(res.isFailure)
        assertTrue(res.exceptionOrNull() is IllegalStateException)
    }

}