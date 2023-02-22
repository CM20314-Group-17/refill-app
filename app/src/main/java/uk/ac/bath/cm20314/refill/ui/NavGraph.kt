package uk.ac.bath.cm20314.refill.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.bath.cm20314.refill.ui.categories.CategoriesScreen
import uk.ac.bath.cm20314.refill.ui.category.CategoryScreen

/** Defines the app's screens and displays the current screen. */
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    // Lambda functions that navigate to each screen.
    // Some screens require arguments, such as the id of the record to retrieve from the database.
    val navigateToCategories = {
        navController.navigate(route = "categories") {
            launchSingleTop = true
        }
    }
    val navigateToCategory = { categoryId: String ->
        navController.navigate(route = "category/$categoryId") {
            launchSingleTop = true
        }
    }
    val navigateToProduct = { productId: String ->
        navController.navigate(route = "product/$productId") {
            launchSingleTop = true
        }
    }
    val navigateToSettings = {
        navController.navigate(route = "settings") {
            launchSingleTop = true
        }
    }

    // The NavHost displays the current screen based on the navController.
    // See https://developer.android.com/jetpack/compose/navigation.
    NavHost(
        navController = navController,
        startDestination = "categories",
    ) {
        // Each 'composable' is a separate screen.
        // The route is used to navigate to each screen, similar to a URL.
        composable(route = "categories") {
            CategoriesScreen(
                navigateToCategory = navigateToCategory,
                navigateToSettings = navigateToSettings
            )
        }
        composable(
            route = "category/{categoryId}",
            arguments = listOf(navArgument(name = "categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")!!
            CategoryScreen(
                categoryId = categoryId,
                navigateToProduct = navigateToProduct)
        }
        composable(
            route = "product/{productId}",
            arguments = listOf(navArgument(name = "productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")!!
            // TODO: Add product screen.
        }
        composable(route = "settings") {
            SettingsScreen(navigateBack = { navController.popBackStack() })
        }
    }
}