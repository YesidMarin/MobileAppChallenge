package com.yesidmarin.mobileappchallenge

import com.yesidmarin.mobileappchallenge.data.ProductRepository
import com.yesidmarin.mobileappchallenge.data.response.ProductDetail
import com.yesidmarin.mobileappchallenge.domain.ProductDetailUseCase
import com.yesidmarin.mobileappchallenge.helpers.CoroutineRule
import com.yesidmarin.mobileappchallenge.helpers.dummyProductDetail
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import java.io.IOException

class ProductDetailUseCaseTest {

    private val repo: ProductRepository = mockk()
    private val useCase = ProductDetailUseCase(repo)

    @get:Rule
    private val coroutineRule = CoroutineRule()

    @Test
    fun `invoke returns repository result`() = runTest(coroutineRule.dispatcher) {
        val detail = dummyProductDetail(id = "ML123")
        val expected = Result.success(detail)

        // Mock del repositorio
        coEvery { repo.getProductDetails("ML123") } returns expected

        val result = useCase("ML123")

        assertTrue(result.isSuccess)
        assertEquals(detail, result.getOrNull())

        // Verifica que realmente delegó al repo
        coVerify(exactly = 1) { repo.getProductDetails("ML123") }
    }

    @Test
    fun `invoke propagates failure from repository`() = runTest {
        val failure = Result.failure<ProductDetail>(IOException("Network down"))
        coEvery { repo.getProductDetails("FAIL") } returns failure

        val res = useCase("FAIL")

        assertTrue(res.isFailure)
        assertTrue(res.exceptionOrNull() is IOException)
        coVerify(exactly = 1) { repo.getProductDetails("FAIL") }
    }
}