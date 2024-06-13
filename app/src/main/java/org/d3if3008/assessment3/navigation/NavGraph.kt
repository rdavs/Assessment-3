package org.d3if3008.assessment3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3008.assessment3.ui.screen.AboutScreen
import org.d3if3008.assessment3.ui.screen.BuktiScreen
import org.d3if3008.assessment3.ui.screen.KEY_ID_TRAVEL
import org.d3if3008.assessment3.ui.screen.MainScreen
import org.d3if3008.assessment3.ui.screen.TravelScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
        composable(route = Screen.FormTravel.route) {
            TravelScreen(navController)
        }
        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_TRAVEL) {
                    type = NavType.LongType
                }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_TRAVEL)
            TravelScreen(navController, id)
        }
        composable(route = Screen.bukti.route) {
            BuktiScreen(navController)
        }
    }
}