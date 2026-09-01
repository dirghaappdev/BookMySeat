package com.dirgha.bookmyseat.utils

import android.content.Context

object AppUtils {

    fun getAppVersion(context: Context): String {

        return try {

            context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName ?: "1.0.0"

        } catch (e: Exception) {
            "1.0.0"
        }

    }

}