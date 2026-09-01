package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.model.CreateTripRequest
import com.dirgha.bookmyseat.repository.AdminRepository
import com.dirgha.bookmyseat.data.local.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val repository = AdminRepository()

    private val _tripCreated = MutableStateFlow(false)
    val tripCreated: StateFlow<Boolean> = _tripCreated

    private val _message =
        MutableStateFlow("")

    val message: StateFlow<String> =
        _message

    fun createTrip(
        route: String,
        date: String,
        timeSlot: String,
        fare: Double,
        seats: Int
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.createTrip(
                        "Bearer ${SessionManager.token}",
                        CreateTripRequest(
                            route,
                            date,
                            timeSlot,
                            fare,
                            seats
                        )
                    )

                if (response.isSuccessful) {

                    _message.value =
                        "Trip Created Successfully"
                    _tripCreated.value = true
                } else {

                    _message.value =
                        "Failed To Create Trip"
                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Error"
            }
        }
    }
    fun resetTripCreatedState() {
        _tripCreated.value = false
        _message.value = ""
    }

}

