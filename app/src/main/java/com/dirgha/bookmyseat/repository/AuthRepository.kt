package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.model.ChangePasswordRequest
import com.dirgha.bookmyseat.data.model.FCMTokenRequest
import com.dirgha.bookmyseat.data.model.ForgotPasswordRequest
import com.dirgha.bookmyseat.data.model.LoginRequest
import com.dirgha.bookmyseat.data.model.RegisterOtpRequest
import com.dirgha.bookmyseat.data.model.RegisterRequest
import com.dirgha.bookmyseat.data.model.RegisterVerifyRequest
import com.dirgha.bookmyseat.data.model.ResetPasswordRequest
import com.dirgha.bookmyseat.data.model.VerifyOtpRequest
import com.dirgha.bookmyseat.data.remote.RetrofitClient
import com.dirgha.bookmyseat.data.remote.RetrofitClient.api
import org.json.JSONObject
import kotlin.String

class AuthRepository {

    suspend fun login(
        phoneNo: String,
        password: String,
        deviceId: String
    ) = RetrofitClient.api.login(
        LoginRequest(
            phoneNo,
            password,
            deviceId
        )
    )

    suspend fun register(
        name: String,
        phoneNo: String,
        password: String,
        email: String
    ): Result<String> {

        return try {

            val response = RetrofitClient.api.register(
                RegisterRequest(
                    name = name,
                    phoneNo = phoneNo,
                    email = email,
                    password = password
                )
            )

            if (response.isSuccessful) {

                Result.success(
                    response.body()?.get("message") ?: "Registration successful"
                )

            } else {

                val errorMessage = try {

                    val errorJson =
                        JSONObject(response.errorBody()?.string() ?: "")

                    errorJson.optString(
                        "detail",
                        "Registration failed"
                    )

                } catch (e: Exception) {

                    "Registration failed"

                }

                Result.failure(Exception(errorMessage))
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun saveFcmToken(

        token:String,

        fcmToken:String

    )= RetrofitClient.api.saveFcmToken(

        token,

        FCMTokenRequest(fcmToken)

    )

    suspend fun changePassword(

        request: ChangePasswordRequest

    )= api.changePassword(request)

    suspend fun registerSendOtp(
        request: RegisterOtpRequest
    ) =
        api.registerSendOtp(request)


    suspend fun registerVerifyOtp(
        request: RegisterVerifyRequest
    ) =
        api.registerVerifyOtp(request)

    suspend fun verifyForgotPasswordOtp(

        email: String,

        otp: String

    ) = api.verifyForgotPasswordOtp(

        VerifyOtpRequest(

            email,

            otp

        )

    )
    suspend fun sendForgotPasswordOtp(

        email: String

    ) = api.sendForgotPasswordOtp(

        ForgotPasswordRequest(

            email

        )

    )

    suspend fun resetPassword(

        email:String,

        password:String

    )=api.resetPassword(

        ResetPasswordRequest(

            email,

            password

        )

    )
}