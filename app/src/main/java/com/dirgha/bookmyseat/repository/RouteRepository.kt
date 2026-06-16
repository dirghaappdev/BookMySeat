package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.remote.RetrofitClient

class RouteRepository {

    suspend fun getRoutes() =
        RetrofitClient.api.getRoutes()
}