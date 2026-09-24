package com.dirgha.bookmyseat.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.viewmodel.ProfileViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material3.HorizontalDivider
import android.util.Patterns
import androidx.compose.material.icons.filled.BarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(

    navController: NavController,

    onLogout: () -> Unit

) {

    val vm: ProfileViewModel = viewModel()

    val profile by vm.profile.collectAsState()

    val context = LocalContext.current
    val isAdmin = SessionManager.role.equals("admin", ignoreCase = true)
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAppInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.loadProfile()
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "My Profile",
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

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            item {

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF1976D2),
                                    Color(0xFF42A5F5)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = profile?.name ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = profile?.phoneNo ?: "",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = profile?.email ?: "",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

            }

            item {

                ElevatedButton(

                    onClick = {
                        showEditDialog = true
                    },

                    shape = RoundedCornerShape(14.dp)

                ) {

                    Icon(Icons.Default.Edit, null)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text("Edit Profile")

                }

                Spacer(modifier = Modifier.height(24.dp))

            }

            item {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    Column {

                        ListItem(
                            headlineContent = {
                                Text("Share App")
                            },
                            leadingContent = {
                                Icon(Icons.Outlined.Share, null)
                            },
                            trailingContent = {
                                Icon(Icons.Default.KeyboardArrowRight, null)
                            },
                            modifier = Modifier.clickable {

                                val intent = Intent(Intent.ACTION_SEND)

                                intent.type = "text/plain"

                                intent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out BookMySeat\n\nhttps://play.google.com/store/apps/details?id=com.dirgha.bookmyseat"
                                )

                                context.startActivity(
                                    Intent.createChooser(
                                        intent,
                                        "Share App"
                                    )
                                )

                            }
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = {
                                Text("App Information")
                            },
                            leadingContent = {
                                Icon(Icons.Outlined.Info, null)
                            },
                            trailingContent = {
                                Icon(Icons.Default.KeyboardArrowRight, null)
                            },
                            modifier = Modifier.clickable {
                                showAppInfoDialog = true
                            }
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = {
                                Text("Help & Support")
                            },
                            leadingContent = {
                                Icon(Icons.Default.Help, null)
                            },
                            trailingContent = {
                                Icon(Icons.Default.KeyboardArrowRight, null)
                            },
                            modifier = Modifier.clickable {

                                navController.navigate("support")

                            }
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = {
                                Text("Travel Summary")
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Default.StackedBarChart,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                Icon(
                                    Icons.Default.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {

                                navController.navigate("travel_summary")

                            }
                        )
                        HorizontalDivider()
                        if (isAdmin) {

                            ListItem(
                                headlineContent = {
                                    Text("Analytics")
                                },
                                leadingContent = {
                                    Icon(
                                        Icons.Default.StackedBarChart,
                                        contentDescription = null
                                    )
                                },
                                trailingContent = {
                                    Icon(
                                        Icons.Default.KeyboardArrowRight,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.clickable {

                                    navController.navigate("admin_analytics")

                                }
                            )
                            HorizontalDivider()
                        }

                        ListItem(
                            headlineContent = {
                                Text("Change Password")
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Password,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                Icon(
                                    Icons.Default.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {

                                navController.navigate("change_password")

                            }
                        )
                        HorizontalDivider()
                        ListItem(
                            headlineContent = {
                                Text("Account Settings")
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                Icon(
                                    Icons.Default.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {

                                navController.navigate("account_settings")

                            }
                        )

                    }

                }

                Spacer(modifier = Modifier.height(18.dp))

            }

//            item {
//
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0xFFFFF3F3)
//                    ),
//                    shape = RoundedCornerShape(18.dp)
//                ) {
//
//                    Column {
//
//                        ListItem(
//                            headlineContent = {
//                                Text(
//                                    "Delete Account",
//                                    color = Color.Red
//                                )
//                            },
//                            leadingContent = {
//                                Icon(
//                                    Icons.Default.Delete,
//                                    null,
//                                    tint = Color.Red
//                                )
//                            },
//                            modifier = Modifier.clickable {
//                                showDeleteDialog = true
//                            }
//                        )
//
//                        HorizontalDivider()
//
//                        ListItem(
//                            headlineContent = {
//                                Text(
//                                    "Logout",
//                                    color = Color.Red
//                                )
//                            },
//                            leadingContent = {
//                                Icon(
//                                    Icons.Outlined.Logout,
//                                    null,
//                                    tint = Color.Red
//                                )
//                            },
//                            modifier = Modifier.clickable {
//                                showLogoutDialog = true
//                            }
//                        )
//
//                    }
//
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//            }
        }
    }


// ---------------- EDIT PROFILE ----------------

if (showEditDialog) {

    var name by remember {
        mutableStateOf(profile?.name ?: "")
    }

    var email by remember {
        mutableStateOf(profile?.email ?: "")
    }

    var validationError by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = {
            showEditDialog = false
        },

        title = {
            Text("Edit Profile")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text("Name")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it

                        validationError = ""
                    },
                    label = {
                        Text("Email")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                if (validationError.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = validationError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

        },

        confirmButton = {

            Button(

                onClick = {

                    when {

                        name.trim().isEmpty() -> {
                            validationError = "Name is required."
                        }

                        email.trim().isEmpty() -> {
                            validationError = "Email is required."
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                            validationError = "Please enter a valid email address."
                        }

                        else -> {

                            validationError = ""

                            vm.updateProfile(
                                name = name.trim(),
                                phoneNo = profile?.phoneNo ?: "",
                                email = email.trim()
                            )

                            showEditDialog = false
                        }
                    }

                }

            ) {

                Text("Save")

            }

        },

        dismissButton = {

            TextButton(

                onClick = {
                    showEditDialog = false
                }

            ) {

                Text("Cancel")

            }

        }

    )

}

// ---------------- APP INFO ----------------

if (showAppInfoDialog) {

    AlertDialog(

        onDismissRequest = {
            showAppInfoDialog = false
        },

        title = {
            Text("BookMySeat")
        },

        text = {

            Column {

                Text("Version 1.0.0")

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    "Intercity seat booking platform for daily travellers."
                )

            }

        },

        confirmButton = {

            TextButton(

                onClick = {
                    showAppInfoDialog = false
                }

            ) {

                Text("OK")

            }

        }

    )

}

// ---------------- DELETE ACCOUNT ----------------

if (showDeleteDialog) {

    AlertDialog(

        onDismissRequest = {
            showDeleteDialog = false
        },

        title = {
            Text("Delete Account")
        },

        text = {
            Text(
                "This action cannot be undone. Are you sure?"
            )
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

// ---------------- LOGOUT ----------------

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

}}
