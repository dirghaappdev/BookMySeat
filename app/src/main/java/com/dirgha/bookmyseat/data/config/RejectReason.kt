package com.dirgha.bookmyseat.data.config

data class RejectReason(
    val id: Int,
    val code: String,
    var name: String,
    val is_active: Boolean
)