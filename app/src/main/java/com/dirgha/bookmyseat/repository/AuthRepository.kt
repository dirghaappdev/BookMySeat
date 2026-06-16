package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.model.LoginRequest
import com.dirgha.bookmyseat.data.model.RegisterRequest
import com.dirgha.bookmyseat.data.remote.RetrofitClient

class AuthRepository {

    suspend fun login(
        phoneNo: String,
        password: String
    ) = RetrofitClient.api.login(
        LoginRequest(
            phoneNo,
            password
        )
    )

    suspend fun register(
        name: String,
        phoneNo: String,
        password: String
    ) = RetrofitClient.api.register(
        RegisterRequest(
            name,
            phoneNo,
            password
        )
    )
}