package com.dirgha.bookmyseat.data.local

import android.content.Context

object SessionManager {

    var token: String = ""
    var role: String = ""
    var userId: String = ""
    var phone: String? = null

    var themeMode = ThemeMode.SYSTEM

    private const val PREF_NAME = "bookmyseat_session"

    fun save(context: Context) {

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        prefs.edit()
            .putString("token", token)
            .putString("role", role)
            .putString("userId", userId)
            .putString("phone", phone)
            .apply()
    }

    fun load(context: Context) {

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        token = prefs.getString("token", "") ?: ""
        role = prefs.getString("role", "") ?: ""
        userId = prefs.getString("userId", "") ?: ""
        phone = prefs.getString("phone", null)
    }

    fun clear() {

        token = ""
        role = ""
        userId = ""
        phone = null
    }

    fun logout(context: Context) {

        clear()

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        prefs.edit().clear().apply()
    }
}