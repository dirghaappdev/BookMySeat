package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.model.CreateTripRequest
import com.dirgha.bookmyseat.data.remote.RetrofitClient

class AdminRepository {

    suspend fun createTrip(
        token: String,
        request: CreateTripRequest
    ) =
        RetrofitClient.api.createTrip(
            token,
            request
        )
}