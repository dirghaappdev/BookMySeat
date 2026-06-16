package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _message =
        MutableStateFlow("")

    val message: StateFlow<String> =
        _message

    fun register(
        name: String,
        phone: String,
        password: String
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.register(
                        name,
                        phone,
                        password
                    )

                if (response.isSuccessful) {

                    _message.value =
                        "Registration Successful"

                } else {

                    _message.value =
                        "Registration Failed"
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
        onSuccess: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response =
                    repository.login(
                        phone,
                        password
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
    }
