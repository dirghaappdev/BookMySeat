package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(

    navController: NavController,

    onLogout: () -> Unit

) {

    val vm: ProfileViewModel = viewModel()

    val context = androidx.compose.ui.platform.LocalContext.current

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Account Settings",
                        fontWeight = FontWeight.Bold
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
                            null
                        )

                    }

                }

            )

        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F8FD))
                .padding(padding)
                .padding(18.dp)

        ) {

            Card(

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1976D2)
                )

            ) {

                Column(

                    modifier = Modifier.padding(20.dp)

                ) {

                    Text(

                        "Security",

                        color = Color.White,

                        fontSize = 22.sp,

                        fontWeight = FontWeight.Bold

                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(

                        "Manage your account and security settings.",

                        color = Color.White.copy(.9f)

                    )

                }

            }

            Spacer(modifier = Modifier.height(22.dp))

            SettingsItem(
                icon = Icons.Default.Password,
                iconBackground = Color(0xFFE3F2FD),
                iconTint = Color(0xFF1976D2),
                title = "Change Password",
                subtitle = "Update your account password"
            ) {
                navController.navigate("change_password")
            }

            Spacer(modifier = Modifier.height(12.dp))

            SettingsItem(
                icon = Icons.Outlined.Logout,
                iconBackground = Color(0xFFFFF4E5),
                iconTint = Color(0xFFFF9800),
                title = "Logout",
                subtitle = "Sign out from this device"
            ) {
                showLogoutDialog = true
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(

                "Danger Zone",

                color = Color.Red,

                fontWeight = FontWeight.Bold,

                fontSize = 16.sp

            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(

                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3F3)
                ),

                shape = RoundedCornerShape(18.dp)

            ) {

                SettingsItem(
                    icon = Icons.Default.DeleteForever,
                    iconBackground = Color(0xFFFFEBEE),
                    iconTint = Color(0xFFD32F2F),
                    title = "Delete Account",
                    subtitle = "This action cannot be undone"
                ) {
                    showDeleteDialog = true
                }

            }

        }

    }

    // Logout Dialog

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {
                showLogoutDialog = false
            },

            title = {
                Text("Logout")
            },

            text = {
                Text("Are you sure you want to logout?")
            },

            confirmButton = {

                Button(

                    onClick = {

                        SessionManager.logout(context)

                        onLogout()

                    }

                ) {

                    Text("Logout")

                }

            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showLogoutDialog = false

                    }

                ) {

                    Text("Cancel")

                }

            }

        )

    }

    // Delete Dialog

    if (showDeleteDialog) {

        AlertDialog(

            onDismissRequest = {
                showDeleteDialog = false
            },

            title = {
                Text("Delete Account")
            },

            text = {
                Text("This action cannot be undone. Are you sure?")
            },

            confirmButton = {

                Button(

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),

                    onClick = {

                        vm.deleteProfile {

                            SessionManager.logout(context)

                            onLogout()

                        }

                    }

                ) {

                    Text("Delete")

                }

            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showDeleteDialog = false

                    }

                ) {

                    Text("Cancel")

                }

            }

        )

    }

}
@Composable
fun SettingsItem(

    icon: ImageVector,

    iconBackground: Color,

    iconTint: Color,

    title: String,

    subtitle: String,

    onClick: () -> Unit

) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },

        shape = RoundedCornerShape(18.dp),

        elevation = CardDefaults.cardElevation(3.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Box(

                modifier = Modifier
                    .size(54.dp)
                    .background(
                        iconBackground,
                        CircleShape
                    ),

                contentAlignment = Alignment.Center

            ) {

                Icon(

                    imageVector = icon,

                    contentDescription = null,

                    tint = iconTint,

                    modifier = Modifier.size(28.dp)

                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(

                    text = title,

                    fontWeight = FontWeight.Bold,

                    fontSize = 17.sp

                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(

                    text = subtitle,

                    color = Color.Gray,

                    fontSize = 13.sp

                )

            }

            Icon(

                Icons.Default.KeyboardArrowRight,

                contentDescription = null,

                tint = Color(0xFFB0BEC5)

            )

        }

    }

}