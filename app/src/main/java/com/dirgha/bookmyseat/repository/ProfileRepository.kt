package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.model.UpdateProfileRequest
import com.dirgha.bookmyseat.data.remote.RetrofitClient

class ProfileRepository {

    suspend fun getProfile() =
        RetrofitClient.api.getProfile()

    suspend fun updateProfile(
        request: UpdateProfileRequest
    ) = RetrofitClient.api.updateProfile(request)

    suspend fun deleteProfile() =
        RetrofitClient.api.deleteProfile()

}