package uk.ac.bath.cm20314.refill.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.bath.cm20314.refill.data.category.Category
import uk.ac.bath.cm20314.refill.data.product.Product
import uk.ac.bath.cm20314.refill.ui.categories.CategoriesScreen
import uk.ac.bath.cm20314.refill.ui.category.CategoryScreen
import uk.ac.bath.cm20314.refill.ui.product.ProductScreen
import uk.ac.bath.cm20314.refill.ui.search.SearchScreen
import uk.ac.bath.cm20314.refill.ui.settings.SettingsScreen

/** Defines the app's screens and displays the current screen. */
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    // Lambda functions that navigate to each screen.
    val navigateToCategories = {
        navController.navigate(route = "categories") {
            popUpTo(route = "categories") {
                inclusive = true
            }
        }
    }
    val navigateToCategory = { category: Category ->
        navController.navigate(route = "category/${category.categoryName}") {
            popUpTo(route = "category") {
                inclusive = true
            }
        }
    }
    val navigateToProduct = { product: Product ->
        navController.navigate(route = "product/${product.categoryName}/${product.productName}") {
            popUpTo(route = "product") {
                inclusive = true
            }
        }
    }
    val navigateToSettings = {
        navController.navigate(route = "settings") {
            popUpTo(route = "settings") {
                inclusive = true
            }
        }
    }
    val navigateToSearch = {
        navController.navigate(route = "search") {
            popUpTo(route = "search") {
                inclusive = true
            }
        }
    }
    val navigateBack = {
        navController.popBackStack()
        Unit
    }

    // The NavHost displays the current screen based on the navController.
    NavHost(
        navController = navController,
        startDestination = "categories"
    ) {
        composable(route = "categories") {
            CategoriesScreen(
                navigateToCategory = navigateToCategory,
                navigateToSettings = navigateToSettings,
                navigateToSearch = navigateToSearch
            )
        }
        composable(
            route = "category/{categoryName}",
            arguments = listOf(navArgument(name = "categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryScreen(
                categoryName = backStackEntry.arguments?.getString("categoryName")!!,
                navigateToProduct = navigateToProduct,
                navigateBack = navigateBack
            )
        }
        composable(
            route = "product/{categoryName}/{productName}",
            arguments = listOf(
                navArgument(name = "categoryName") { type = NavType.StringType },
                navArgument(name = "productName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ProductScreen(
                categoryName = backStackEntry.arguments?.getString("categoryName")!!,
                productName = backStackEntry.arguments?.getString("productName")!!,
                navigateBack = navigateBack
            )
        }
        composable(route = "settings") {
            SettingsScreen(
                navigateToCategories = navigateToCategories
            )
        }
        composable(route = "search") {
            SearchScreen(
                navigateBack = navigateBack,
                navigateToProduct = { product ->
                    navigateToCategories()
                    navigateToProduct(product)
                }
            )
        }
    }
}