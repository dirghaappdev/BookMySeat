package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.remote.RetrofitClient

class NotificationRepository {

    suspend fun getNotifications() =
        RetrofitClient.api.getNotifications()

    suspend fun markRead(notificationId: String) =
        RetrofitClient.api.markNotificationRead(notificationId)

    suspend fun markAllRead() =
        RetrofitClient.api.markAllNotificationsRead()
}