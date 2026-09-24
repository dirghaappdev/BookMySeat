package com.dirgha.bookmyseat.navigation


import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirgha.bookmyseat.ui.screens.OtpType
import com.dirgha.bookmyseat.ui.screens.OtpVerificationScreen
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dirgha.bookmyseat.data.local.SessionManager.token
import com.dirgha.bookmyseat.ui.screens.AccountSettingsScreen
import com.dirgha.bookmyseat.ui.screens.AdminAnalyticsScreen
import com.dirgha.bookmyseat.viewmodel.AuthViewModel
import com.dirgha.bookmyseat.ui.screens.AdminHomeScreen
import com.dirgha.bookmyseat.ui.screens.AllTripsScreen
import com.dirgha.bookmyseat.ui.screens.BookingReviewScreen
import com.dirgha.bookmyseat.ui.screens.BookingSearchScreen
import com.dirgha.bookmyseat.ui.screens.BookingSuccessScreen
import com.dirgha.bookmyseat.ui.screens.ChangePasswordScreen
import com.dirgha.bookmyseat.ui.screens.CreateTripScreen
import com.dirgha.bookmyseat.ui.screens.ForgotPasswordScreen
import com.dirgha.bookmyseat.ui.screens.MemberHomeScreen
import com.dirgha.bookmyseat.ui.screens.LoginScreen
import com.dirgha.bookmyseat.ui.screens.ManageBookingsScreen
import com.dirgha.bookmyseat.ui.screens.ManageParcelBookingsScreen
import com.dirgha.bookmyseat.ui.screens.MyBookingsScreen
import com.dirgha.bookmyseat.ui.screens.MyParcelsScreen
import com.dirgha.bookmyseat.ui.screens.NotificationScreen
import com.dirgha.bookmyseat.ui.screens.ParcelBookingScreen
import com.dirgha.bookmyseat.ui.screens.ParcelReviewScreen
import com.dirgha.bookmyseat.ui.screens.ParcelSuccessScreen
import com.dirgha.bookmyseat.ui.screens.ProfileScreen
import com.dirgha.bookmyseat.ui.screens.RegisterScreen
import com.dirgha.bookmyseat.ui.screens.ResetPasswordScreen
import com.dirgha.bookmyseat.ui.screens.SplashScreen
import com.dirgha.bookmyseat.ui.screens.SupportScreen
import com.dirgha.bookmyseat.ui.screens.TravelSummaryScreen
import com.dirgha.bookmyseat.ui.screens.TripSuccessScreen
import com.dirgha.bookmyseat.ui.viewmodel.ParcelViewModel
import com.dirgha.bookmyseat.utils.SessionExpiredManager
import com.dirgha.bookmyseat.viewmodel.AdminViewModel
import com.dirgha.bookmyseat.viewmodel.MyBookingViewModel
import com.dirgha.bookmyseat.viewmodel.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {

    val navController =
        rememberNavController()

    val sessionExpired by
    SessionExpiredManager
        .sessionExpired
        .collectAsState()

    val context = LocalContext.current

    LaunchedEffect(sessionExpired) {

        if (sessionExpired) {

            Toast.makeText(
                context,
                "Session expired. Please login again.",
                Toast.LENGTH_LONG
            ).show()

            navController.navigate(
                Screen.Login.route
            ) {
                popUpTo(0)
            }

            SessionExpiredManager.reset()
        }
    }

    val viewModel: AuthViewModel =
        viewModel()
    val adminViewModel: AdminViewModel =
        viewModel()
    val myBookingViewModel: MyBookingViewModel =
        viewModel()
    val notificationViewModel : NotificationViewModel=
        viewModel()
    val parcelViewModel : ParcelViewModel =
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


/// ============================================================
// PARCEL BOOKING
// ============================================================

        composable(
            Screen.ParcelBookingScreen.route
        ) {

            ParcelBookingScreen(

                parcelViewModel =
                    parcelViewModel,

                onBackClick = {

                    navController.popBackStack()
                },

                onContinueClick = {

                    navController.navigate(
                        Screen.ParcelReview.route
                    )
                }
            )
        }


// ============================================================
// PARCEL REVIEW
// ============================================================

        composable(
            Screen.ParcelReview.route
        ) {

            ParcelReviewScreen(

                parcelViewModel =
                    parcelViewModel,

                navController =
                    navController
            )
        }


// ============================================================
// PARCEL SUCCESS
// ============================================================

        composable(
            route =
                Screen.ParcelSuccess.route
        ) { backStackEntry ->

            val parcelId =
                backStackEntry
                    .arguments
                    ?.getString("parcelId")
                    ?: ""

            val status =
                backStackEntry
                    .arguments
                    ?.getString("status")
                    ?: "PENDING"

            ParcelSuccessScreen(

                parcelId =
                    parcelId,

                status =
                    status,

                onBookAnother = {

                    parcelViewModel.clearParcelDraft()

                    navController.navigate(
                        Screen.ParcelBookingScreen.route
                    ) {

                        popUpTo(
                            Screen.ParcelSuccess.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },

                onDone = {

                    parcelViewModel.clearParcelDraft()

                    navController.navigate(
                        Screen.MemberHome.route
                    ) {

                        popUpTo(
                            Screen.ParcelSuccess.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
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
            Screen.MyBookings.route
        ) {

            MyBookingsScreen(navController)
        }

        composable(
            Screen.ProfileScreen.route
        ) {

            ProfileScreen(

                navController = navController,

                onLogout = {

                    navController.navigate(Screen.Login.route) {

                        popUpTo(0)

                    }

                }

            )

        }
        composable(
            "booking_success/{bookingId}/{status}"
        ) { backStackEntry ->

            BookingSuccessScreen(
                navController = navController,
                bookingId = backStackEntry.arguments?.getString("bookingId") ?: "",
                status = backStackEntry.arguments?.getString("status") ?: ""
            )
        }
        composable(
            Screen.ManageBookings.route
        ) {

            ManageBookingsScreen(navController)
        }

        composable(
            Screen.MemberHome.route
        ) {

            MemberHomeScreen( navController , myBookingViewModel, notificationViewModel)
        }
        composable(
            Screen.CreateTrip.route
        ) {

            CreateTripScreen(navController)
        }
        composable(
            "booking_review/{tripId}/{route}/{date}/{time}/{passengerCount}/{fare}/{bookingType}"
        ) { backStackEntry ->

            BookingReviewScreen(
                navController = navController,

                tripId = backStackEntry.arguments?.getString("tripId")!!,

                route = backStackEntry.arguments?.getString("route")!!,

                date = backStackEntry.arguments?.getString("date")!!,

                time = backStackEntry.arguments?.getString("time")!!,

                passengerCount = backStackEntry.arguments
                    ?.getString("passengerCount")
                    ?.toInt() ?: 1,

                totalFare = backStackEntry.arguments
                    ?.getString("fare")
                    ?.toDoubleOrNull() ?: 0.0,

                bookingType = backStackEntry.arguments
                    ?.getString("bookingType") ?: "RIDE"
            )
        }
        composable(
            route = Screen.MyParcels.route
        ) {
            MyParcelsScreen(
                navController = navController
            )
        }
        composable("admin_analytics") {

            AdminAnalyticsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.ManageParcelBookings.route
        ) {
            val parcelViewModel: ParcelViewModel = viewModel()

            ManageParcelBookingsScreen(
                parcelViewModel = parcelViewModel
            )
        }
        composable(Screen.TravelSummary.route) {
            TravelSummaryScreen(navController)
        }
        composable(Screen.AllTrips.route) {
            AllTripsScreen(navController)
        }
        composable(Screen.Notifications.route){

            NotificationScreen(
                navController

                //token = "Bearer $token"

            )

        }
        composable(

            Screen.ChangePassword.route

        ) {

            ChangePasswordScreen(
                navController
            )

        }
        composable("account_settings") {

            AccountSettingsScreen(

                navController = navController,

                onLogout = {

                    navController.navigate(Screen.Login.route) {

                        popUpTo(0)

                    }

                }

            )

        }
        composable(
            Screen.Support.route
        ) {
            SupportScreen(navController)
        }
        composable(

            route = Screen.OtpVerification.route

        ) { backStackEntry ->

            val type =

                backStackEntry.arguments
                    ?.getString("type")
                    ?: OtpType.REGISTER.name

            OtpVerificationScreen(

                navController = navController,

                type = OtpType.valueOf(type),

                viewModel = viewModel

            )

        }
        composable(Screen.ResetPassword.route) {

            ResetPasswordScreen(

                navController = navController,

                viewModel = viewModel

            )

        }
        composable(
            Screen.ForgotPassword.route
        ) {

            ForgotPasswordScreen(

                navController = navController,

                viewModel = viewModel

            )

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

