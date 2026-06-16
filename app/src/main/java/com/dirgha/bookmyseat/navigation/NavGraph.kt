package com.dirgha.bookmyseat.navigation


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dirgha.bookmyseat.viewmodel.AuthViewModel
import com.dirgha.bookmyseat.ui.screens.AdminHomeScreen
import com.dirgha.bookmyseat.ui.screens.BookingFormScreen
import com.dirgha.bookmyseat.ui.screens.BookingSearchScreen
import com.dirgha.bookmyseat.ui.screens.CreateTripScreen
import com.dirgha.bookmyseat.ui.screens.MemberHomeScreen
import com.dirgha.bookmyseat.ui.screens.LoginScreen
import com.dirgha.bookmyseat.ui.screens.ManageBookingsScreen
import com.dirgha.bookmyseat.ui.screens.MyBookingsScreen
import com.dirgha.bookmyseat.ui.screens.RegisterScreen
import com.dirgha.bookmyseat.ui.screens.SplashScreen
import com.dirgha.bookmyseat.ui.screens.TripSuccessScreen
import com.dirgha.bookmyseat.viewmodel.AdminViewModel

@Composable
fun NavGraph() {

    val navController =
        rememberNavController()

    val viewModel: AuthViewModel =
        viewModel()
    val adminViewModel: AdminViewModel =
        viewModel()
    NavHost(
        navController = navController,
        startDestination =
            Screen.Splash.route
    ) {
        composable(
            Screen.BookingSearch.route
        ) {
            BookingSearchScreen(
                navController
            )
        }
        composable(
            Screen.Splash.route
        ) {

            SplashScreen(
                navController
            )
        }

        composable(
            Screen.Login.route
        ) {

            LoginScreen(
                navController,
                viewModel
            )
        }

        composable(
            Screen.Register.route
        ) {

            RegisterScreen(
                navController,
                viewModel
            )
        }

        composable(
            Screen.AdminHome.route
        ) {

            AdminHomeScreen(
                navController
            )
        }
        composable(
            route = "booking_form/{tripId}"
        ) {

            val tripId =
                it.arguments?.getString("tripId")
                    ?: ""

            BookingFormScreen(
                navController,
                tripId
            )
        }

        composable(
            Screen.MyBookings.route
        ) {

            MyBookingsScreen()
        }
        composable(
            Screen.ManageBookings.route
        ) {

            ManageBookingsScreen()
        }

        composable(
            Screen.MemberHome.route
        ) {

            MemberHomeScreen( navController)
        }
        composable(
            Screen.CreateTrip.route
        ) {

            CreateTripScreen(navController)
        }

        composable(
            "trip_success/{message}",
            arguments = listOf(navArgument("message") { type = NavType.StringType })
        ) { backStackEntry ->
            val msg = Uri.decode(backStackEntry.arguments?.getString("message") ?: "")
            TripSuccessScreen(navController, msg)
        }
    }
}