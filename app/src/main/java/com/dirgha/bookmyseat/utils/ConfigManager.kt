package com.dirgha.bookmyseat.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import com.dirgha.bookmyseat.data.config.ConfigResponse
import com.google.gson.Gson

private val Context.dataStore by preferencesDataStore("config")

class ConfigManager(private val context: Context) {

    companion object {

        private val CONFIG_JSON =
            stringPreferencesKey("config_json")

    }

    suspend fun saveConfig(json: String) {

        context.dataStore.edit {

            it[CONFIG_JSON] = json

        }

    }

    suspend fun getConfig(): String? {

        val pref = context.dataStore.data.first()

        return pref[CONFIG_JSON]

    }

    suspend fun getConfiguration(): ConfigResponse? {

        val json = getConfig()

        return if (json != null) {
            Gson().fromJson(
                json,
                ConfigResponse::class.java
            )
        } else {
            null
        }
    }

}