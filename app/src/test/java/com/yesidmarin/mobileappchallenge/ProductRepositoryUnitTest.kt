package com.yesidmarin.mobileappchallenge

import com.yesidmarin.mobileappchallenge.data.ProductRepository
import com.yesidmarin.mobileappchallenge.helpers.CoroutineRule
import com.yesidmarin.mobileappchallenge.helpers.dummyProductDetail
import com.yesidmarin.mobileappchallenge.helpers.dummyProducts
import com.yesidmarin.mobileappchallenge.network.MercadoLibreApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import java.io.IOException

@RunWith(org.junit.runners.JUnit4::class)
class ProductRepositoryUnitTest {

    @get:Rule
    private val coroutineRule = CoroutineRule()

    private val api: MercadoLibreApi = mockk()
    private lateinit var repo: ProductRepository

    @Before
    fun setUp() {
        repo = ProductRepository(api)
    }

    @Test
    fun searchProducts_success_returnsResultSuccess() = runTest(coroutineRule.dispatcher) {
        // Simula una respuesta exitosa de la API
        val expected = dummyProducts(total = 3)

        // API retorna 200 OK
        coEvery {
            api.searchProducts(
                siteId  = "MCO",
                status  = "active",
                productIdentifier = null,
                query   = "iphone",
                limit   = 10,
                offset  = 0
            )
        } returns Response.success(expected)


        val result = repo.searchProducts(
            siteId = "MCO",
            status = "active",
            query  = "iphone",
            limit  = 10,
            offset = 0
        )

        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrNull()?.paging?.total)
    }


    @Test
    fun searchProducts_httpError_returnsResultFailure() = runTest(coroutineRule.dispatcher) {
        coEvery { api.searchProducts(any(), any(), any(), any(), any(), any()) }
            .returns(
                Response.error(
                    500,
                    "Boom".toResponseBody("text/plain".toMediaType())
                )
            )

        val result = repo.searchProducts("MCO", "active")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("500"))
    }

    @Test
    fun `getProductDetails success returns Result_Success`() = runTest(coroutineRule.dispatcher) {
        // Simula una respuesta exitosa de la API
        val detail = dummyProductDetail(id = "ML123")

        coEvery { api.getProductDetails("ML123") } returns Response.success(detail)

        val res = repo.getProductDetails("ML123")

        assertTrue(res.isSuccess)
        assertEquals(detail, res.getOrNull())
    }

    @Test
    fun getProductDetails_exception_returnsResultFailure() = runTest(coroutineRule.dispatcher) {
        coEvery { api.getProductDetails("123") } throws IOException("Network down")

        val result = repo.getProductDetails("123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }


}