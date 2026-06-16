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
        passengerCount: Int,
        gender: String,
        note: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.createBooking(
                        token,
                        CreateBookingRequest(
                            tripId,
                            passengerCount,
                            gender,
                            note
                        )
                    )

                if (response.isSuccessful) {

                    _message.value =
                        "Booking Created"

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