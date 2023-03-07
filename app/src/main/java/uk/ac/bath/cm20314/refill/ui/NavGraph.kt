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
import uk.ac.bath.cm20314.refill.ui.settings.SettingsScreen

/** Defines the app's screens and displays the current screen. */
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    // Lambda functions that navigate to each screen.
    // Some screens require arguments, such as the id of the record to retrieve from the database.
    val navigateToCategory = { category: Category ->
        navController.navigate(route = "category/${category.categoryId}") {
            launchSingleTop = true
        }
    }
    val navigateToProduct = { product: Product ->
        navController.navigate(route = "product/${product.categoryId}/${product.productId}") {
            launchSingleTop = true
        }
    }
    val navigateToSettings = {
        navController.navigate(route = "settings") {
            launchSingleTop = true
        }
    }
    val navigateToSearch = {
        navController.navigate(route = "search") {
            launchSingleTop = true
        }
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
            route = "category/{categoryId}",
            arguments = listOf(navArgument(name = "categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryScreen(
                categoryId = backStackEntry.arguments?.getString("categoryId")!!,
                navigateToProduct = navigateToProduct,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "product/{categoryId}/{productId}",
            arguments = listOf(
                navArgument(name = "categoryId") { type = NavType.StringType },
                navArgument(name = "productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ProductScreen(
                categoryId = backStackEntry.arguments?.getString("categoryId")!!,
                productId = backStackEntry.arguments?.getString("productId")!!,
                navigateBack = { navController.popBackStack() })
        }
        composable(route = "settings") {
            SettingsScreen(navigateBack = { navController.popBackStack() })
        }
    }
}