package com.dirgha.bookmyseat.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dirgha.bookmyseat.data.model.Notification
import com.dirgha.bookmyseat.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {

    val vm: NotificationViewModel = viewModel()

    val notifications by vm.notifications.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadNotifications()
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.titleLarge
                    )

                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )

                    }

                },

                actions = {

                    TextButton(

                        onClick = {

                            vm.markAllRead()

                        }

                    ) {

                        Text("Read All")

                    }

                }

            )

        }

    ) { padding ->
        if (notifications.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                        tint = Color.Gray
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "No Notifications Yet",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        "Booking updates will appear here.",
                        color = Color.Gray
                    )

                }

            }

        } else {


        LazyColumn(

            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),

            contentPadding = PaddingValues(
                horizontal = 14.dp,
                vertical = 12.dp
            ),

            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {

            items(

                items = notifications,

                key = { it.notificationId }

            ) { item ->

                NotificationItem(

                    item = item,

                    onClick = {

                        if (!item.isRead) {
                            vm.markRead(item.notificationId)
                        }

                        when (item.click_action) {

                            "MANAGE_BOOKINGS" -> {
                                navController.navigate("manage_bookings")
                            }

                            "OPEN_BOOKING" -> {
                                navController.navigate("my_bookings")
                            }

                            "OPEN_PARCEL" -> {
                                navController.navigate("my_parcels")
                            }

                            "MANAGE_PARCELS" -> {
                                navController.navigate("manage_parcel_bookings")
                            }

                        }

                    }

                )

            }
        }
    }
}}
@Composable
fun NotificationIcon(item: Notification) {

    val icon: ImageVector
    val color: Color

    when (item.type) {

        "BOOKING_CONFIRMED" -> {
            icon = Icons.Default.CheckCircle
            color = Color(0xFF4CAF50)
        }

        "BOOKING_REJECTED" -> {
            icon = Icons.Default.Cancel
            color = Color(0xFFF44336)
        }

        "BOOKING_CREATED" -> {
            icon = Icons.Default.DirectionsCar
            color = Color(0xFF2196F3)
        }

        "USER_REGISTERED" -> {
            icon = Icons.Default.PersonAdd
            color = Color(0xFFFF9800)
        }

        "OFFER" -> {
            icon = Icons.Default.CardGiftcard
            color = Color(0xFF9C27B0)
        }

        else -> {
            icon = Icons.Default.Notifications
            color = MaterialTheme.colorScheme.primary
        }

    }

    Box(

        modifier = Modifier
            .size(54.dp)
            .background(
                color.copy(alpha = .15f),
                CircleShape
            ),

        contentAlignment = Alignment.Center

    ) {

        Icon(

            imageVector = icon,

            contentDescription = null,

            tint = color

        )

    }

}
@Composable
fun NotificationItem(

    item: Notification,

    onClick: () -> Unit

) {

    ElevatedCard(

        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },

        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation =
                if(item.isRead) 1.dp else 5.dp
        ),

        shape = RoundedCornerShape(18.dp)

    ) {

        Column(

            modifier = Modifier.padding(16.dp)

        ) {

            Row(

                verticalAlignment = Alignment.CenterVertically

            ) {

                NotificationIcon(item)

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(

                        text = item.title,

                        fontWeight = FontWeight.Bold,

                        style = MaterialTheme.typography.titleMedium

                    )

                }

                Spacer(Modifier.width(8.dp))

                Text(

                    text = formatNotificationTime(item.createdAt),

                    style = MaterialTheme.typography.labelSmall,

                    color = Color.Gray

                )

                if(!item.isRead){

                    Spacer(Modifier.width(8.dp))

                    Box(

                        Modifier
                            .size(10.dp)
                            .background(
                                Color(0xFF2196F3),
                                CircleShape
                            )

                    )

                }

            }

            Spacer(Modifier.height(12.dp))

            Text(

                text = item.body,

                style = MaterialTheme.typography.bodyMedium,

                color = Color.DarkGray

            )

        }

    }

}
fun formatNotificationTime(time: String): String {

    return try {

        val parser =

            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                Locale.getDefault()
            )

        parser.timeZone = TimeZone.getTimeZone("UTC")

        val date = parser.parse(time) ?: return time

        val diff = System.currentTimeMillis() - date.time

        val minutes = diff / 60000
        val hours = diff / 3600000
        val days = diff / 86400000

        when {

            minutes < 1 -> "Just now"

            minutes < 60 -> "$minutes min ago"

            hours < 24 -> "$hours hr ago"

            days == 1L -> "Yesterday"

            days < 7 -> "$days days ago"

            else -> {

                SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                ).format(date)

            }

        }

    } catch (e: Exception) {

        time

    }

}

