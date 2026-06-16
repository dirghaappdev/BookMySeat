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
    object BookingForm : Screen("booking_form/{tripId}")
    object ManageBookings : Screen("manage_bookings")
    object MyBookings : Screen("my_bookings")
}