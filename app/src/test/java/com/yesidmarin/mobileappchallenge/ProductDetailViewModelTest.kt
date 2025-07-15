package com.yesidmarin.mobileappchallenge

import app.cash.turbine.test
import com.yesidmarin.mobileappchallenge.domain.ProductDetailUseCase
import com.yesidmarin.mobileappchallenge.helpers.CoroutineRule
import com.yesidmarin.mobileappchallenge.helpers.dummyProductDetail
import com.yesidmarin.mobileappchallenge.ui.viewmodel.ProductDetailViewModel
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Assert.*
import java.io.IOException
import kotlin.test.Test

class ProductDetailViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val useCase: ProductDetailUseCase = mockk()
    private lateinit var vm: ProductDetailViewModel

    @Before fun setup() {
        vm = ProductDetailViewModel(useCase)
    }

    @Test
    fun `getProductDetails with empty id sets errorState`() =  runTest {
        vm.errorState.test {
            vm.getProductDetails("")

            assertNull(awaitItem())
            assertEquals("El ID del producto no se encontró", awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        coVerify { useCase wasNot Called }
    }


    @Test
    fun `getProductDetails success fills flows`() = runTest(coroutineRule.dispatcher) {
        val detail = dummyProductDetail(id = "ML123")
        coEvery { useCase("ML123") } returns Result.success(detail)

        vm.getProductDetails("ML123")

        vm.productDetail.test {
            assertNull(awaitItem())
            val result = awaitItem()
            assertEquals(detail, result)
        }

        vm.images.test {
            assertEquals(
                detail.pictures!!.mapNotNull { it.url },
                awaitItem()
            )
            cancelAndConsumeRemainingEvents()
        }

        vm.productName.test {
            assertEquals(detail.name, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        assertEquals("ML123", vm.productId.value)
        assertNull(vm.errorState.value)
        coVerify(exactly = 1) { useCase("ML123") }
    }

    @Test
    fun `getProductDetails failure sets errorState`() = runTest(coroutineRule.dispatcher) {
        coEvery { useCase("FAIL") } returns Result.failure(IOException("Network"))

        vm.errorState.test {
            vm.getProductDetails("FAIL")
            assertNull(awaitItem())
            assertEquals("Network", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { useCase("FAIL") }
    }

}