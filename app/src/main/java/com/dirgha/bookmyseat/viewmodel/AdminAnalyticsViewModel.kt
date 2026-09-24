package com.dirgha.bookmyseat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.remote.RetrofitClient
import com.dirgha.bookmyseat.ui.model.AdminAnalyticsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminAnalyticsViewModel : ViewModel() {

    private val _analytics =
        MutableStateFlow<AdminAnalyticsResponse?>(null)

    val analytics: StateFlow<AdminAnalyticsResponse?> =
        _analytics

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> =
        _errorMessage

    fun loadAnalytics(
        period: String,
        year: Int? = null,
        month: Int? = null
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null

            try {

                val response =
                    RetrofitClient.api.getAdminAnalytics(
                        period = period,
                        year = year,
                        month = month
                    )

                _analytics.value = response

            } catch (e: Exception) {

                _errorMessage.value =
                    e.message ?: "Unable to load analytics"

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun refresh(
        period: String,
        year: Int? = null,
        month: Int? = null
    ) {

        loadAnalytics(
            period = period,
            year = year,
            month = month
        )
    }
}