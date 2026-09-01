package com.dirgha.bookmyseat.data.config

data class ConfigData(
    val config: Config,
    val masters: Masters
)

data class Config(
    val version: VersionConfig,
    val support: SupportConfig
)

data class Masters(
    val rejectReason: List<RejectReason>,
    val bookingRules: List<BookingRule>
)