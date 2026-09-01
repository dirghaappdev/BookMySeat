package com.dirgha.bookmyseat.data.model



data class Notification(

    val notificationId: String,

    val title: String,

    val body: String,

    val type: String,

    val click_action: String,

    val color: String,

    val isRead: Boolean,

    val createdAt: String

)