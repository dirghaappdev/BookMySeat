package com.dirgha.bookmyseat.data.model

//data class Booking(
//    val bookingId: String,
//    val tripId: String,
//    val userId: String,
//    val passengerCount: Int,
//    val gender: String,
//    val note: String,
//    val totalFare: Double,
//    val bookingStatus: String
//)

data class Booking(
    val bookingId: String,
    val userName: String,
    val tripId: String,
    val route: String,
    val date: String,
    val timeSlot: String,
    val fare: Double,
    val passengerCount: Int,
    val gender: String,
    val bookingStatus: String
)

data class CreateBookingRequest(
    val tripId: String,
    val passengerCount: Int,
    val gender: String,
    val note: String
)

data class BookingResponse(
    val bookingId: String,
    val bookingStatus: String,
    val totalFare: Double
)