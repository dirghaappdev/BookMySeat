package com.dirgha.bookmyseat.utils

import android.content.Context

object InternetManager {

    fun requireInternet(
        context: Context
    ): Boolean {

        return NetworkUtils
            .isInternetAvailable(context)
    }
}