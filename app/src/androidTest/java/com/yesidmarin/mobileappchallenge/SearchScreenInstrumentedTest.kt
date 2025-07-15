package com.yesidmarin.mobileappchallenge

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.yesidmarin.mobileappchallenge.ui.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchScreenInstrumentedTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()

    @Before fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun searchScreen_togglesAdvancedSection() {
        // Estado inicial
        composeRule.onNodeWithText("Búsqueda avanzada").assertIsDisplayed()
        composeRule.onNodeWithText("Buscar").assertIsDisplayed()

        // Abre sección avanzada
        composeRule.onNodeWithText("Búsqueda avanzada").performClick()
        composeRule.onNodeWithText("Ocultar búsqueda avanzada").assertIsDisplayed()

        // Controles avanzados
        composeRule.onNodeWithText("País").assertExists()
        composeRule.onNodeWithText("Estado").assertExists()
    }

    @Test
    fun emptyQuery_showsError() {
        composeRule.onNodeWithTag("SearchBtn").performClick()

        composeRule.onNodeWithText("Completa este campo para buscar")
            .assertIsDisplayed()
    }
}
