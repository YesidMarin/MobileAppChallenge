package com.yesidmarin.mobileappchallenge.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yesidmarin.mobileappchallenge.ui.components.DropdownMenu
import com.yesidmarin.mobileappchallenge.ui.components.ProductIdentifierCheckbox
import com.yesidmarin.mobileappchallenge.ui.components.SearchField
import com.yesidmarin.mobileappchallenge.ui.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    // Define the options for the dropdown menus
    val siteIds = mapOf(
        "MLA" to "Argentina",
        "MCO" to "Colombia"
    )

    var status = mapOf(
        "active" to "Activo",
        "inactive" to "Inactivo"
    )

    // state variables for the search functionality
    var query by rememberSaveable { mutableStateOf("") }
    var queryError by rememberSaveable { mutableStateOf(false) }
    var isAdvancedSearchVisible by rememberSaveable { mutableStateOf(false) }
    var siteIdSelect by rememberSaveable { mutableStateOf(siteIds.keys.last()) }
    var statusSelect by rememberSaveable { mutableStateOf(status.keys.first()) }
    var isProductIdentifier by rememberSaveable { mutableStateOf(false) }

    val navigationEvent by viewModel.navigationEvent.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Handle navigation events
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { destination ->
            navController.navigate(destination)
            viewModel.clearNavigationEvent()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Búsqueda de productos") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(8.dp),
        )  {
            // Search field with focus management
            SearchField(
                query = query,
                queryError = queryError,
                onQueryChange = { query = it },
                focusRequester = focusRequester,
                onFocusClear = { queryError = query.isBlank() }
            )

            // Button to toggle advanced search options
            TextButton(
                onClick = { isAdvancedSearchVisible = !isAdvancedSearchVisible }
            ) {
                Text(if (isAdvancedSearchVisible) "Ocultar búsqueda avanzada" else "Búsqueda avanzada")
            }

            AnimatedVisibility(
                visible = isAdvancedSearchVisible,
                enter = expandVertically() + fadeIn(),
                exit  = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))

                    // Dropdown menus and checkbox for advanced search options
                    DropdownMenu(
                        label = "País",
                        options = siteIds,
                        selectedOption = siteIds.filter { it.key == siteIdSelect }.values.first(),
                        onOptionSelected = { siteIdSelect = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    DropdownMenu(
                        label = "Estado",
                        options = status,
                        selectedOption = status.filter { it.key == statusSelect }.values.first(),
                        onOptionSelected = { statusSelect = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ProductIdentifierCheckbox(
                        isChecked = isProductIdentifier,
                        onCheckedChange = { isProductIdentifier = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search button to trigger the search action
            Button(
                onClick = {
                    if (query.isBlank()) {
                        queryError = true
                        focusRequester.requestFocus()
                    } else {
                        queryError = false
                        focusManager.clearFocus()
                        keyboardController?.hide()

                        // Navigate to results screen with the search parameters
                        viewModel.navigateTo("results_screen?q=$query&siteId=$siteIdSelect&status=$statusSelect&productIdentifier=$isProductIdentifier")
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .testTag("SearchBtn"),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Buscar")
            }
        }
    }
}