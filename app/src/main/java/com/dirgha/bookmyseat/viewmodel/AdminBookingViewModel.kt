package com.dirgha.bookmyseat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.model.Booking
import com.dirgha.bookmyseat.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "AdminBookingViewModel"

class AdminBookingViewModel : ViewModel() {

    private val repository =
        BookingRepository()

    private val _bookings =
        MutableStateFlow<List<Booking>>(
            emptyList()
        )

    val bookings: StateFlow<List<Booking>> =
        _bookings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // NEW: surfaces what actually went wrong instead of failing silently.
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun loadBookings() {

        viewModelScope.launch {

            _isLoading.value = true
            try {

                val response =
                    repository.getAllBookings(
                        "Bearer ${SessionManager.token}"
                    )

                if (response.isSuccessful) {

                    _bookings.value =
                        response.body()
                            ?: emptyList()

                    _error.value = ""

                } else {
                    // THIS branch was previously empty - a non-2xx response
                    // (401/403/404/500...) was silently ignored, leaving
                    // `bookings` stuck at emptyList() with no indication why.
                    val errorBody = try {
                        response.errorBody()?.string()
                    } catch (e: Exception) {
                        null
                    }

                    val msg = "Failed to load bookings (HTTP ${response.code()})" +
                            if (!errorBody.isNullOrBlank()) ": $errorBody" else ""

                    Log.e(TAG, msg)
                    _error.value = msg
                }

            } catch (e: Exception) {

                Log.e(TAG, "loadBookings() threw an exception", e)
                _error.value = e.message ?: "Something went wrong while loading bookings"
            }finally {

                _isLoading.value = false
            }
        }
    }

    fun confirmBooking(
        bookingId: String
    ) {

        viewModelScope.launch {

            try {
                val response = repository.confirmBooking(
                    "Bearer ${SessionManager.token}",
                    bookingId
                )

                if (!response.isSuccessful) {
                    Log.e(
                        TAG,
                        "confirmBooking failed (HTTP ${response.code()}): ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "confirmBooking() threw an exception", e)
            }

            loadBookings()
        }
    }

    fun rejectBooking(
        bookingId: String,
        reason: String
    ) {

        viewModelScope.launch {

            try {
                val response = repository.rejectBooking(
                    "Bearer ${SessionManager.token}",
                    bookingId,
                    reason
                )

                if (!response.isSuccessful) {
                    Log.e(
                        TAG,
                        "rejectBooking failed (HTTP ${response.code()}): ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "rejectBooking() threw an exception", e)
            }

            loadBookings()
        }
    }
}