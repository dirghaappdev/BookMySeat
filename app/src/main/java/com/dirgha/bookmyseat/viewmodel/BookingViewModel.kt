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

    private val _message =
        MutableStateFlow("")

    val message: StateFlow<String> =
        _message

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
                println("STATUS = ${response.code()}")
                println("BODY = ${response.body()}")
                println("ERROR = ${response.errorBody()?.string()}")

                if (response.isSuccessful) {

                    _message.value = "Booking Created"

                    onSuccess()

                } else {

                    _message.value =
                        response.errorBody()?.string()
                            ?: "Booking Failed"
                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Error"
            }
        }
    }
}