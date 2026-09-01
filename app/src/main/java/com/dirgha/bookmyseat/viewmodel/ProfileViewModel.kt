package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.model.Profile
import com.dirgha.bookmyseat.data.model.UpdateProfileRequest
import com.dirgha.bookmyseat.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _profile =
        MutableStateFlow<Profile?>(null)

    val profile: StateFlow<Profile?>
        get() = _profile

    private val _message =
        MutableStateFlow("")

    val message: StateFlow<String>
        get() = _message

    fun loadProfile() {

        viewModelScope.launch {

            val response =
                repository.getProfile()

            if (response.isSuccessful) {

                _profile.value =
                    response.body()

            }

        }

    }

    fun updateProfile(

        name: String,

        phoneNo: String,

        email: String

    ) {

        viewModelScope.launch {

            val response = repository.updateProfile(
                UpdateProfileRequest(
                    name = name,
                    email = email
                )
            )

            Log.d("PROFILE", "HTTP = ${response.code()}")
            Log.d("PROFILE", "BODY = ${response.body()}")
            Log.d("PROFILE", "ERROR = ${response.errorBody()?.string()}")

            if (response.isSuccessful) {

                _message.value = "Profile Updated"
                loadProfile()

            } else {

                _message.value = "Update Failed"
            }
        }

    }

    fun deleteProfile(

        onDeleted: () -> Unit

    ) {

        viewModelScope.launch {

            val response =
                repository.deleteProfile()

            if (response.isSuccessful) {

                onDeleted()

            }

        }

    }

}