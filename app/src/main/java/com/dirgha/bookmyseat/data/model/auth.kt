package com.dirgha.bookmyseat.data.model

data class RegisterRequest(
    val name: String,
    val phoneNo: String,
    val email: String,
    val password: String

)

data class LoginRequest(
    val phoneNo: String,
    val password: String,
    val deviceId: String
)

data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String
)

data class ChangePasswordRequest(

    val oldPassword: String,

    val newPassword: String

)

data class RegisterOtpRequest(
    val name: String,
    val phoneNo: String,
    val email: String,
    val password: String
)

data class RegisterVerifyRequest(
    val name: String,
    val phoneNo: String,
    val email: String,
    val password: String,
    val otp: String
)

data class VerifyOtpRequest(

    val email: String,

    val otp: String

)

data class ResetPasswordRequest(

    val email:String,

    val password:String

)

data class ForgotPasswordRequest(

    val email: String

)