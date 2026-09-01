package com.dirgha.bookmyseat.repository


import com.dirgha.bookmyseat.data.remote.RetrofitClient
import com.dirgha.bookmyseat.data.config.ConfigResponse
import retrofit2.Response

class ConfigurationRepository {

    suspend fun getConfiguration(): Response<ConfigResponse> {
        return RetrofitClient.api.getConfiguration()
    }
}