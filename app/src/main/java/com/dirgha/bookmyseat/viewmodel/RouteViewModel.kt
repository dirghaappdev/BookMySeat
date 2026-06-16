package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.model.Route
import com.dirgha.bookmyseat.repository.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RouteViewModel : ViewModel() {

    private val repository =
        RouteRepository()

    private val _routes =
        MutableStateFlow<List<Route>>(
            emptyList()
        )

    val routes: StateFlow<List<Route>> =
        _routes

    fun loadRoutes() {

        viewModelScope.launch {

            try {

                val response =
                    repository.getRoutes()

                if (response.isSuccessful) {

                    _routes.value =
                        response.body()
                            ?: emptyList()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}