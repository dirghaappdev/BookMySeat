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
    val mobileNumber: String = "",
    val tripId: String,
    val route: String,
    val date: String,
    val timeSlot: String,
    val fare: Double,
    val note: String = "",
    val passengerCount: Int,
    val gender: String,
    val bookingStatus: String,
    val rejectionReason: String = "",
    val totalSeats: Int = 0,
    val bookedPassengers: Int = 0,
    val availableSeats: Int = 0,
    val overBookedSeats: Int = 0,
    val isOverBooked: Boolean = false
)

data class CreateBookingRequest(
    val tripId: String,
    val passengerCount: Int,
    val gender: String,
    val note: String,

    val bookingType: String = "RIDE",
    val parcelCount: Int = 0,
    val parcelType: String = "",
    val parcelWeight: String = ""
)

data class BookingResponse(
    val bookingId: String,
    val bookingStatus: String,
    val totalFare: Double
)

data class RejectBookingRequest(
    val reason: String
)