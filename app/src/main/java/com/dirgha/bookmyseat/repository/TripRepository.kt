package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.model.CreateTripRequest
import com.dirgha.bookmyseat.data.model.SearchTripRequest
import com.dirgha.bookmyseat.data.remote.RetrofitClient

class TripRepository {

    suspend fun createTrip(
        route: String,
        date: String,
        timeSlot: String,
        fare: Double,
        totalSeats: Int
    ) =
        RetrofitClient.api.createTrip(
            "Bearer ${SessionManager.token}",
            CreateTripRequest(
                route = route,
                date = date,
                timeSlot = timeSlot,
                fare = fare,
                totalSeats = totalSeats
            )
        )

    suspend fun getAllTrips() =
        RetrofitClient.api.getAllTrips()

    suspend fun searchTrips(
        route: String,
        date: String
    ) =
        RetrofitClient.api.searchTrips(
            SearchTripRequest(
                route,
                date
            )
        )

}