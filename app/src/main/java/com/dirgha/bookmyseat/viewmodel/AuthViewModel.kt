package com.dirgha.bookmyseat.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.model.ChangePasswordRequest
import com.dirgha.bookmyseat.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.messaging.FirebaseMessaging
import com.dirgha.bookmyseat.data.model.RegisterOtpRequest
import com.dirgha.bookmyseat.data.model.RegisterVerifyRequest
import com.dirgha.bookmyseat.data.model.VerifyOtpRequest
import com.dirgha.bookmyseat.data.remote.RetrofitClient.api
import com.dirgha.bookmyseat.ui.screens.OtpType

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()
    var registerName = ""
    var registerPhone = ""
    var registerEmail = ""
    var registerPassword = ""
    private val _message =
        MutableStateFlow("")

    val message: StateFlow<String> =
        _message

    private val _changePasswordMessage =
        MutableStateFlow("")

    var changePasswordMessage by mutableStateOf("")
        private set

    fun register(
        name: String,
        phone: String,
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.register(
                        name,
                        phone,
                        password,
                        email
                    )

                if (response.isSuccess) {

                    _message.value =
                        response.getOrNull() ?: "Registration successful"

                } else {

                    _message.value =
                        response.exceptionOrNull()?.message ?: "Registration failed"

                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Error"
            }
        }
    }


    fun login(
        phone: String,
        password: String,
        deviceId: String,
        onSuccess: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.login(
                        phone,
                        password,
                        deviceId
                    )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        SessionManager.token =
                            body.token

                        SessionManager.role =
                            body.role

                        SessionManager.userId =
                            body.userId

                        _message.value =
                            "Login Successful"
                        onSuccess(body.role)

                        FirebaseMessaging.getInstance()
                            .token
                            .addOnSuccessListener { token ->

                                Log.d("FCM", "Token = $token")

                                viewModelScope.launch {

                                    try {

                                        val response = repository.saveFcmToken(
                                            "Bearer ${SessionManager.token}",
                                            token
                                        )

                                        Log.d("FCM", "HTTP = ${response.code()}")

                                        Log.d("FCM", "Body = ${response.body()}")
                                        Log.d("FCM", "ERROR = ${response.errorBody()?.string()}")

                                        if (!response.isSuccessful) {
                                            Log.e(
                                                "FCM",
                                                response.errorBody()?.string() ?: "Unknown error"
                                            )
                                        }

                                    } catch (e: Exception) {

                                        Log.e("FCM", "Exception", e)

                                    }

                                }

                            }

                        onSuccess(body.role)
                    }

                } else {

                    _message.value =
                        "Invalid Credentials"
                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Error"
            }
        }
    }
    fun clearMessage() {
        _message.value = ""
    }

    fun changePassword(

        oldPassword:String,

        newPassword:String

    ){

        viewModelScope.launch {

            try{

                val response=

                    repository.changePassword(

                        ChangePasswordRequest(

                            oldPassword,

                            newPassword

                        )

                    )

                if(response.isSuccessful){

                    changePasswordMessage =
                        response.body()?.message ?: "Password changed successfully"

                }else{

                    changePasswordMessage =
                        response.errorBody()?.string() ?: "Unable to change password"

                }

            }catch (e:Exception){

                _changePasswordMessage.value=

                    e.message ?: "Something went wrong"

            }

        }

    }
    fun registerSendOtp(
        name: String,
        phone: String,
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            try {
                registerName = name
                registerPhone = phone
                registerEmail = email
                registerPassword = password
                val response = repository.registerSendOtp(

                    RegisterOtpRequest(

                        name = name,
                        phoneNo = phone,
                        email = email,
                        password = password

                    )

                )

                if (response.isSuccessful) {

                    _message.value =
                        response.body()?.message ?: "OTP sent successfully"

                } else {

                    _message.value =
                        response.errorBody()?.string() ?: "Unable to send OTP"

                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Something went wrong"

            }

        }

    }
    fun verifyOtp(

        otp: String,

        type: OtpType,

        onSuccess: () -> Unit

    ) {

        viewModelScope.launch {

            try {

                when (type) {

                    OtpType.REGISTER -> {

                        val response = repository.registerVerifyOtp(

                            RegisterVerifyRequest(

                                name = registerName,
                                phoneNo = registerPhone,
                                email = registerEmail,
                                password = registerPassword,
                                otp = otp

                            )

                        )

                        if (response.isSuccessful) {

                            Log.d("OTP", "SUCCESS = ${response.body()}")

                            _message.value =
                                response.body()?.message ?: "OTP Verified"

                            onSuccess()

                        } else {

                            Log.d("OTP", "HTTP = ${response.code()}")

                            Log.d("OTP", "ERROR = ${response.errorBody()?.string()}")

                            _message.value = "Verification failed"

                        }

                    }

                    OtpType.FORGOT_PASSWORD -> {

                        val response = repository.verifyForgotPasswordOtp(

                            registerEmail,
                            otp
                        )

                        if (response.isSuccessful) {

                            _message.value =
                                response.body()?.message ?: "OTP Verified"

                            onSuccess()

                        } else {

                            _message.value =
                                response.errorBody()?.string()
                                    ?: "Invalid OTP"

                        }

                    }

                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Something went wrong"

            }

        }

    }
    fun resetPassword(
        password: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            try {

                Log.d("RESET", "Email = $registerEmail")
                Log.d("RESET", "Password = $password")

                val response = repository.resetPassword(
                    registerEmail,
                    password
                )

                Log.d("RESET", "HTTP = ${response.code()}")

                if (response.isSuccessful) {

                    Log.d("RESET", "BODY = ${response.body()}")

                    _message.value =
                        response.body()?.message ?: "Password changed successfully"

                    onSuccess()

                } else {

                    Log.d("RESET", "ERROR = ${response.errorBody()?.string()}")

                    _message.value = "Reset failed"

                }

            } catch (e: Exception) {

                Log.e("RESET", "Exception", e)

                _message.value = e.message ?: "Something went wrong"

            }

        }

    }

    fun sendForgotPasswordOtp(

        email: String

    ) {

        registerEmail = email

        viewModelScope.launch {

            try {

                val response = repository.sendForgotPasswordOtp(email)

                if (response.isSuccessful) {

                    _message.value =
                        response.body()?.message ?: "OTP sent successfully"

                } else {

                    _message.value =
                        response.errorBody()?.string() ?: "Unable to send OTP"

                }

            } catch (e: Exception) {

                _message.value =
                    e.message ?: "Something went wrong"

            }

        }

    }


}
