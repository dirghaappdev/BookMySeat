package com.dirgha.bookmyseat.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionExpiredManager {

    private val _sessionExpired =
        MutableStateFlow(false)

    val sessionExpired =
        _sessionExpired.asStateFlow()

    fun expireSession() {
        _sessionExpired.value = true
    }

    fun reset() {
        _sessionExpired.value = false
    }
}