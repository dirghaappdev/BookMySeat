package com.dirgha.bookmyseat.navigation

sealed class Screen(
    val route: String
) {

    object Splash : Screen("splash")

    object Login : Screen("login")

    object Register : Screen("register")

    object AdminHome : Screen("admin_home")

    object MemberHome : Screen("member_home")
    object SearchTrip : Screen("search_trip")
    object CreateTrip : Screen("create_trip")
    object BookingSearch : Screen("booking_search")
    object ParcelBookingScreen :
        Screen("parcel_booking")

    object ParcelReview :
        Screen("parcel_review")

    object ParcelSuccess :
        Screen("parcel_success/{parcelId}") {

        fun createRoute(parcelId: String): String {
            return "parcel_success/$parcelId"
        }
    }
    object MyParcels : Screen("my_parcels")
    object ManageParcelBookings : Screen("manage_parcel_bookings")
    object BookingForm : Screen("booking_form/{tripId}")
    object ManageBookings : Screen("manage_bookings")
    object MyBookings : Screen("my_bookings")
    object AllTrips : Screen("all_trips")
    object ProfileScreen: Screen("my_profile")
    object Notifications : Screen("notifications")

    object TravelSummary : Screen("travel_summary")
    object ChangePassword : Screen("change_password")
    object OtpVerification :
        Screen("otp_verification/{type}") {

        fun createRoute(type: String) =
            "otp_verification/$type"
    }
    object AccountSettings : Screen("account_settings")
    object ForgotPassword : Screen("forgot_password")
    object Support : Screen("support")
    object ResetPassword : Screen("reset_password")
}