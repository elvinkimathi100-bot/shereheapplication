package com.mark.shereheke.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mark.shereheke.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.EventDetail.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            EventDetailScreen(navController, backStackEntry.arguments?.getString("eventId"))
        }
        composable(
            route = Screen.Checkout.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            CheckoutScreen(navController, backStackEntry.arguments?.getString("eventId"))
        }
        composable(
            route = Screen.Payment.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            PaymentScreen(navController, backStackEntry.arguments?.getString("eventId"))
        }
        composable(Screen.MyTickets.route) {
            MyTicketsScreen(navController)
        }
        composable(Screen.HotelDashboard.route) {
            HotelDashboardScreen(navController)
        }
        composable(Screen.CreateEvent.route) {
            AddEventScreen(navController)
        }

        composable(Screen.EventDetail.route ) {
            AddEventScreen(navController)
        }
    }
}
