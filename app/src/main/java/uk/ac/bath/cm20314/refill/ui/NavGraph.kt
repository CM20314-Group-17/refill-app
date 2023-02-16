package uk.ac.bath.cm20314.refill.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/** Defines the app's screens and displays the current screen. */
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navigateToCategories = {
        navController.navigate(route = "categories")
    }
    val navigateToCategory = { categoryId: String ->
        navController.navigate(route = "category/$categoryId")
    }
    val navigateToProduct = { productId: String ->
        navController.navigate(route = "product/$productId")
    }
    val navigateToSettings = {
        navController.navigate(route = "settings")
    }

    NavHost(
        modifier = modifier.statusBarsPadding(),
        navController = navController,
        startDestination = "categories",
    ) {
        composable(route = "categories") {
            Column {
                Text(text = "Categories Screen")
                Button(onClick = { navigateToCategory("test") }) {
                    Text(text = "Go to Category Screen")
                }
                Button(onClick = { navigateToProduct("test") }) {
                    Text(text = "Go to Product Screen")
                }
                Button(onClick = navigateToSettings) {
                    Text(text = "Go to Settings Screen")
                }
            }
        }
        composable(
            route = "category/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) {
            Column {
                Text(text = "Category Screen")
                Button(onClick = navigateToCategories) {
                    Text(text = "Go back")
                }
            }
        }
        composable(
            route = "product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            Column {
                Text(text = "Product Screen")
                Button(onClick = navigateToCategories) {
                    Text(text = "Go back")
                }
            }
        }
        composable(route = "settings") {
            Column {
                Text(text = "Settings Screen")
                Button(onClick = navigateToCategories) {
                    Text(text = "Go back")
                }
            }
        }
    }
}