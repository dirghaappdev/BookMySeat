package com.dirgha.bookmyseat.data.remote

import com.dirgha.bookmyseat.network.AuthInterceptor
import com.dirgha.bookmyseat.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}