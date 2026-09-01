package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.model.Booking
import com.dirgha.bookmyseat.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyBookingViewModel : ViewModel() {

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
    fun loadMyBookings() {

        viewModelScope.launch {
            _isLoading.value = true
            try {

                val response =
                    repository.getMyBookings(
                        "Bearer ${SessionManager.token}"
                    )

                if (response.isSuccessful) {

                    _bookings.value =
                        response.body()
                            ?: emptyList()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }finally {

                _isLoading.value = false
            }
        }
    }
}