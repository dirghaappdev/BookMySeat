package com.dirgha.bookmyseat.data.config

data class ConfigResponse(
    val status: Int,
    val message: String,
    val errorMessage: String,
    val data: ConfigData
)