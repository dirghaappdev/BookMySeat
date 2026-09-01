package com.dirgha.bookmyseat.data.config

data class VersionConfig(
    val latestVersion: String,
    val minimumSupportedVersion: String,
    val softUpdate: Boolean,
    val forceUpdate: Boolean,
    val playStoreUrl: String,
    val updateTitle: String,
    val updateMessage: String
)