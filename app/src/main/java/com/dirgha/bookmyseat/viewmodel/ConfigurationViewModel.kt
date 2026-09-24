package com.dirgha.bookmyseat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.config.ConfigResponse
import com.dirgha.bookmyseat.repository.ConfigurationRepository
import com.dirgha.bookmyseat.utils.ConfigManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfigurationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ConfigurationRepository()

    private val _configuration =
        MutableStateFlow<ConfigResponse?>(null)

    val configuration: StateFlow<ConfigResponse?> =
        _configuration

    private val _loading =
        MutableStateFlow(false)

    val loading: StateFlow<Boolean> =
        _loading

    fun loadConfiguration() {

        viewModelScope.launch {

            _loading.value = true

            try {

                Log.d(
                    "CONFIG",
                    "Loading configuration from API"
                )

                val response =
                    repository.getConfiguration()

                Log.d(
                    "CONFIG",
                    "Response code = ${response.code()}"
                )

                if (response.isSuccessful) {

                    val config =
                        response.body()

                    _configuration.value =
                        config

                    config?.let {

                        val json =
                            Gson().toJson(it)

                        ConfigManager(
                            getApplication()
                        ).saveConfig(json)

                        Log.d(
                            "CONFIG",
                            "Configuration saved successfully"
                        )
                    }

                } else {

                    Log.e(
                        "CONFIG",
                        "Configuration API failed = ${response.code()}"
                    )

                    loadCachedConfiguration()
                }

            } catch (e: Exception) {

                Log.e(
                    "CONFIG",
                    "Configuration API exception",
                    e
                )

                loadCachedConfiguration()

            } finally {

                _loading.value = false
            }
        }
    }

    private suspend fun loadCachedConfiguration() {

        try {

            val cachedConfig =
                ConfigManager(
                    getApplication()
                ).getConfiguration()

            if (cachedConfig != null) {

                _configuration.value =
                    cachedConfig

                Log.d(
                    "CONFIG",
                    "Loaded cached configuration"
                )

            } else {

                Log.d(
                    "CONFIG",
                    "No cached configuration available"
                )
            }

        } catch (e: Exception) {

            Log.e(
                "CONFIG",
                "Failed to load cached configuration",
                e
            )
        }
    }
}