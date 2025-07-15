package com.yesidmarin.mobileappchallenge.ui.screens

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yesidmarin.mobileappchallenge.ui.viewmodel.ResultViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import com.yesidmarin.mobileappchallenge.ui.components.ErrorView
import com.yesidmarin.mobileappchallenge.ui.components.LoadingIndicator
import com.yesidmarin.mobileappchallenge.ui.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController, viewModel: ResultViewModel = hiltViewModel(), query: String, siteId: String, status: String, productIdentifier: Boolean) {
    // Collecting state from the ViewModel
    val errorState by viewModel.errorState.collectAsState()
    val productsResult by viewModel.productsResult.collectAsState()
    val offset by viewModel.offset.collectAsState()
    val limit by viewModel.limit.collectAsState()
    val total by viewModel.total.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()


    val lazyGridState = rememberLazyGridState()
    val flingBehavior = ScrollableDefaults.flingBehavior()

    // Handle navigation events
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { destination ->
            navController.navigate(destination)
            viewModel.clearNavigationEvent()
        }
    }

    // Load more products when the user scrolls to the end of the list
    LaunchedEffect(lazyGridState, productsResult) {
        snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo }
            .map { visibleItems ->
                if (visibleItems.isNotEmpty()) visibleItems.last().index else 0
            }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                val totalResults = productsResult?.results?.size ?: 0
                if (lastVisibleIndex >= totalResults - 1 && offset + limit <= total) {
                    viewModel.loadMoreProducts(query, siteId, status, productIdentifier)
                }
            }
    }

    // Trigger initial product search if results are not already loaded
    LaunchedEffect(Unit) {
        if (viewModel.productsResult.value == null) {
            viewModel.searchProducts(query, siteId, status, offset ,limit, productIdentifier)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados de búsqueda") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            // Show loading indicator when productDetail is null and errorState is null
            productsResult == null && errorState == null -> {
                LoadingIndicator()
            }
            // Show error view if there is an error
            errorState != null -> {
                ErrorView(errorState ?: "Ocurrió un error inesperado", "Volver al menú principal") {
                    navController.navigateUp()
                }
            }
            // Show results if they are available
            else -> {
                // Display the products in a grid
                LazyVerticalGrid(
                    state = lazyGridState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(paddingValues),
                    contentPadding = PaddingValues(0.dp),
                    flingBehavior = flingBehavior
                ) {
                    // Get the products from the result and display them
                    productsResult?.results?.let { results ->
                        items(
                            results,
                            key = { it.id }
                        ) { product ->
                            // Show each product in a ProductCard
                            ProductCard(
                                modifier = Modifier,
                                name = product.name,
                                imageUrl = product.pictures.firstOrNull()?.url,
                                onClick = { viewModel.navigateTo("product_detail_screen/${product.id}") }
                            )
                        }
                    }

                    // Show a progress indicator at the bottom when loading more products
                    if (isLoadingMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}