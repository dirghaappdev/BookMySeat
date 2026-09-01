package com.dirgha.bookmyseat.viewmodel

import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.config.ConfigResponse
import com.dirgha.bookmyseat.repository.ConfigurationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.dirgha.bookmyseat.utils.ConfigManager

class ConfigurationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ConfigurationRepository()

    private val _configuration =
        MutableStateFlow<ConfigResponse?>(null)

    val configuration: StateFlow<ConfigResponse?> = _configuration

    fun loadConfiguration() {

        viewModelScope.launch {

            try {

                val response = repository.getConfiguration()

                if (response.isSuccessful) {

                    val config = response.body()

                    _configuration.value = config

                    config?.let {

                        val json = Gson().toJson(it)

                        ConfigManager(getApplication()).saveConfig(json)

                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

}