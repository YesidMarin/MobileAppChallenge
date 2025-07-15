package com.yesidmarin.mobileappchallenge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yesidmarin.mobileappchallenge.ui.screens.ProductDetailScreen
import com.yesidmarin.mobileappchallenge.ui.screens.SearchScreen
import com.yesidmarin.mobileappchallenge.ui.screens.ResultsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search_screen"
    ) {
        composable("search_screen") { SearchScreen(navController) }
        composable(
            route = "results_screen?q={query}&siteId={siteId}&status={status}&productIdentifier={productIdentifier}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType },
                navArgument("siteId") { type = NavType.StringType },
                navArgument("status") { type = NavType.StringType },
                navArgument("productIdentifier") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val siteId = backStackEntry.arguments?.getString("siteId") ?: "MLA"
            val status = backStackEntry.arguments?.getString("status") ?: "active"
            val productIdentifier = backStackEntry.arguments?.getBoolean("productIdentifier") ?: false

            ResultsScreen(navController, query = query, siteId = siteId, status = status, productIdentifier = productIdentifier)
        }
        composable(
            route = "product_detail_screen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("id") ?: ""
             ProductDetailScreen(navController, id = id)
        }
    }
}