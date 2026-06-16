package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.model.Trip
import com.dirgha.bookmyseat.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {

    private val repository = TripRepository()

    private val _trips =
        MutableStateFlow<List<Trip>>(emptyList())

    val trips: StateFlow<List<Trip>> =
        _trips

    private val _message =
        MutableStateFlow("")

    val message: StateFlow<String> =
        _message
    fun loadTrips() {

        viewModelScope.launch {

            try {

                val response =
                    repository.getAllTrips()

                if (response.isSuccessful) {

                    _trips.value =
                        response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchTrips(
        route: String,
        date: String
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.searchTrips(
                        route,
                        date
                    )

                if (response.isSuccessful) {

                    _trips.value =
                        response.body() ?: emptyList()

                } else {

                    _message.value =
                        "No trips found"
                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Error"
            }
        }
    }
}