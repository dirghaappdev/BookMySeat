package com.dirgha.bookmyseat.network

import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.utils.SessionExpiredManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder =
            chain.request().newBuilder()

        if (SessionManager.token.isNotBlank()) {

            requestBuilder.addHeader(
                "Authorization",
                "Bearer ${SessionManager.token}"
            )

        }

        val response =
            chain.proceed(requestBuilder.build())

        if (response.code == 401) {

            SessionManager.clear()

            SessionExpiredManager.expireSession()

        }

        return response

    }

}