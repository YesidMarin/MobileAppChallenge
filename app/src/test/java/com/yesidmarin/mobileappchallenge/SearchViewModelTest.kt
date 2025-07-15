package com.yesidmarin.mobileappchallenge

import app.cash.turbine.test
import com.yesidmarin.mobileappchallenge.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SearchViewModelTest {

    private val vm = SearchViewModel()

    @Test
    fun `navigateTo sets and clears navigationEvent`() = runTest {
        vm.navigationEvent.test {

            assertNull(awaitItem())

            vm.navigateTo("results_screen")
            assertEquals("results_screen", awaitItem())

            vm.clearNavigationEvent()
            assertNull(awaitItem())

            expectNoEvents()
        }
    }
}