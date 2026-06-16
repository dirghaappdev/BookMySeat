package com.dirgha.bookmyseat.data.model

data class RegisterRequest(
    val name: String,
    val phoneNo: String,
    val password: String
)

data class LoginRequest(
    val phoneNo: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String
)