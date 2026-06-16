package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.model.Booking
import com.dirgha.bookmyseat.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminBookingViewModel : ViewModel() {

    private val repository =
        BookingRepository()

    private val _bookings =
        MutableStateFlow<List<Booking>>(
            emptyList()
        )

    val bookings: StateFlow<List<Booking>> =
        _bookings

    fun loadBookings() {

        viewModelScope.launch {

            try {

                val response =
                    repository.getAllBookings(
                        "Bearer ${SessionManager.token}"
                    )

                if (response.isSuccessful) {

                    _bookings.value =
                        response.body()
                            ?: emptyList()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun confirmBooking(
        bookingId: String
    ) {

        viewModelScope.launch {

            repository.confirmBooking(
                "Bearer ${SessionManager.token}",
                bookingId
            )

            loadBookings()
        }
    }

    fun rejectBooking(
        bookingId: String
    ) {

        viewModelScope.launch {

            repository.rejectBooking(
                "Bearer ${SessionManager.token}",
                bookingId
            )

            loadBookings()
        }
    }
}