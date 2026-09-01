package com.dirgha.bookmyseat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirgha.bookmyseat.data.model.Notification
import com.dirgha.bookmyseat.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class NotificationViewModel : ViewModel() {

    private val repository = NotificationRepository()

    private val _notifications =
        MutableStateFlow<List<Notification>>(emptyList())

    val notifications: StateFlow<List<Notification>>
        get() = _notifications

    fun loadNotifications() {

        viewModelScope.launch {

            try {

                val response = repository.getNotifications()

                Log.d("NOTIFICATION", response.code().toString())

                if (response.isSuccessful) {

                    _notifications.value =
                        response.body() ?: emptyList()

                } else {

                    Log.e(
                        "NOTIFICATION",
                        response.errorBody()?.string() ?: ""
                    )

                }

            } catch (e: Exception) {

                Log.e("NOTIFICATION", e.toString())

            }

        }

    }

    fun markRead(notificationId: String) {

        viewModelScope.launch {

            try {

                repository.markRead(notificationId)

                loadNotifications()

            } catch (e: Exception) {

                Log.e("NOTIFICATION", e.toString())

            }

        }

    }
    val unreadCount: Int
        get() = _notifications.value.count { !it.isRead }

    fun markAllRead() {

        viewModelScope.launch {

            try {

                repository.markAllRead()

                loadNotifications()

            } catch (e: Exception) {

                Log.e("NOTIFICATION", e.toString())

            }

        }

    }

}