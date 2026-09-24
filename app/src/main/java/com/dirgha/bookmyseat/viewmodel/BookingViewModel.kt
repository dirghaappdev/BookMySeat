package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.model.CreateBookingRequest
import com.dirgha.bookmyseat.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    private val repository = BookingRepository()

    private val _message = MutableStateFlow("")

    val message: StateFlow<String> = _message

    fun createBooking(
        token: String,
        tripId: String,

        // Ride
        passengerCount: Int,
        gender: String,

        // Parcel
        bookingType: String = "RIDE",
        parcelCount: Int = 0,
        parcelType: String = "",
        parcelWeight: String = "",

        // Common
        note: String,

        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            try {

                _message.value = ""

                println("================================")
                println("CREATE BOOKING STARTED")
                println("TRIP ID = $tripId")
                println("BOOKING TYPE = $bookingType")
                println("================================")

                val response = repository.createBooking(

                    token,

                    CreateBookingRequest(
                        tripId = tripId,
                        passengerCount = passengerCount,
                        gender = gender,
                        note = note,
                        bookingType = bookingType,
                        parcelCount = parcelCount,
                        parcelType = parcelType,
                        parcelWeight = parcelWeight
                    )
                )

                println("================================")
                println("BOOKING API RESPONSE")
                println("STATUS = ${response.code()}")
                println("SUCCESS = ${response.isSuccessful}")
                println("BODY = ${response.body()}")
                println("ERROR = ${response.errorBody()?.string()}")
                println("================================")

                if (response.isSuccessful) {

                    _message.value = "Booking Created"

                    println("BOOKING CREATED SUCCESSFULLY")
                    println("CALLING NAVIGATION CALLBACK")

                    onSuccess()

                    println("NAVIGATION CALLBACK FINISHED")

                } else {

                    val error =
                        response.errorBody()?.string()
                            ?: "Booking Failed"

                    _message.value = error

                    println("BOOKING FAILED")
                    println("ERROR = $error")
                }

            } catch (e: Exception) {

                println("================================")
                println("BOOKING EXCEPTION")
                println("MESSAGE = ${e.message}")
                e.printStackTrace()
                println("================================")

                _message.value =
                    e.message ?: "Something went wrong"
            }
        }
    }
}