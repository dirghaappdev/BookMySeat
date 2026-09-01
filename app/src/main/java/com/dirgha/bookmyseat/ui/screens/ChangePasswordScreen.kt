package com.dirgha.bookmyseat.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController
) {

    val vm: AuthViewModel = viewModel()

    val context = androidx.compose.ui.platform.LocalContext.current

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var oldVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val message = vm.changePasswordMessage

    LaunchedEffect(message) {

        if (message.isEmpty())
            return@LaunchedEffect

        loading = false

        snackbarHostState.showSnackbar(message)

        if (message == "Password changed successfully") {

            val vibrator =
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        150,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )

            }

            MediaPlayer.create(
                context,
                Settings.System.DEFAULT_NOTIFICATION_URI
            )?.start()

            navController.popBackStack()

        }

    }

    Scaffold(

        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Change Password",
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
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp),

            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Default.Security,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Change Password",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Keep your account secure",
                        color = Color.White.copy(.9f),
                        style = MaterialTheme.typography.bodySmall
                    )

                }

            }
                OutlinedTextField(

                    value = oldPassword,

                    onValueChange = {
                        oldPassword = it
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    label = {
                        Text("Current Password")
                    },

                    leadingIcon = {

                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null
                        )

                    },

                    trailingIcon = {

                        IconButton(
                            onClick = {
                                oldVisible = !oldVisible
                            }
                        ) {

                            Icon(

                                if (oldVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,

                                contentDescription = null

                            )

                        }

                    },

                    visualTransformation =
                        if (oldVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),

                    singleLine = true

                )

                OutlinedTextField(

                    value = newPassword,

                    onValueChange = {
                        newPassword = it
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    label = {
                        Text("New Password")
                    },

                    leadingIcon = {

                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null
                        )

                    },

                    trailingIcon = {

                        IconButton(
                            onClick = {
                                newVisible = !newVisible
                            }
                        ) {

                            Icon(

                                if (newVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,

                                contentDescription = null

                            )

                        }

                    },

                    visualTransformation =
                        if (newVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),

                    singleLine = true

                )

                OutlinedTextField(

                    value = confirmPassword,

                    onValueChange = {
                        confirmPassword = it
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    label = {
                        Text("Confirm New Password")
                    },

                    leadingIcon = {

                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null
                        )

                    },

                    trailingIcon = {

                        IconButton(
                            onClick = {
                                confirmVisible = !confirmVisible
                            }
                        ) {

                            Icon(

                                if (confirmVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,

                                contentDescription = null

                            )

                        }

                    },

                    visualTransformation =
                        if (confirmVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),

                    singleLine = true

                )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0)
                ),

                    enabled = !loading,

                    onClick = {

                        when {

                            oldPassword.isBlank() -> {

                                Toast.makeText(
                                    context,
                                    "Enter old password",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            newPassword.length < 6 -> {

                                Toast.makeText(
                                    context,
                                    "Password should be at least 6 characters",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            newPassword != confirmPassword -> {

                                Toast.makeText(
                                    context,
                                    "Passwords do not match",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            else -> {

                                loading = true

                                vm.changePassword(
                                    oldPassword,
                                    newPassword
                                )

                            }

                        }

                     },


                ) {

                    if (loading) {

                        CircularProgressIndicator(

                            modifier = Modifier.size(22.dp),

                            strokeWidth = 2.dp,

                            color = MaterialTheme.colorScheme.onPrimary

                        )

                    } else {

                        Text(
                            "Change Password",
                            style = MaterialTheme.typography.titleMedium
                        )

                    }

                }

                Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(14.dp)
                ) {

                    Text(
                        "Password Tips",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text("✓ Minimum 6 characters", style = MaterialTheme.typography.bodySmall)
                    Text("✓ Mix letters & numbers", style = MaterialTheme.typography.bodySmall)
                    Text("✓ Avoid old passwords", style = MaterialTheme.typography.bodySmall)

                }

            }

            }

        }

    }