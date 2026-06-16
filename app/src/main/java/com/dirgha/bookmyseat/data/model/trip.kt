package com.dirgha.bookmyseat.data.model


data class Trip(
    val tripId: String,
    val route: String,
    val date: String,
    val timeSlot: String,
    val fare: Double,
    val totalSeats: Int,
    val availableSeats: Int,
    val status: String
)
data class CreateTripRequest(
    val route: String,
    val date: String,
    val timeSlot: String,
    val fare: Double,
    val totalSeats: Int
)

data class CreateTripResponse(
    val message: String,
    val tripId: String
)

data class SearchTripRequest(
    val route: String,
    val date: String
)