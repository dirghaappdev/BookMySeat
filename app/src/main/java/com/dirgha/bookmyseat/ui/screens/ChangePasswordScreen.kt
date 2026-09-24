
package com.dirgha.bookmyseat.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        text = "Security",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
                .padding(horizontal = 20.dp)
                .navigationBarsPadding()
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // Security Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                MaterialTheme.colorScheme.primary
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Update your password",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "Use a strong password to keep your account protected.",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                alpha = 0.75f
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Current Password
            PasswordField(
                value = oldPassword,
                onValueChange = {
                    oldPassword = it
                },
                label = "Current password",
                visible = oldVisible,
                onVisibilityChange = {
                    oldVisible = !oldVisible
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New Password
            PasswordField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                },
                label = "New password",
                visible = newVisible,
                onVisibilityChange = {
                    newVisible = !newVisible
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password
            PasswordField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                label = "Confirm new password",
                visible = confirmVisible,
                onVisibilityChange = {
                    confirmVisible = !confirmVisible
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password Tips
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFF8F1)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Password requirements",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    PasswordTip("At least 6 characters")
                    PasswordTip("Use a combination of letters and numbers")
                    PasswordTip("Don't reuse your old password")
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Change Password Button
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(16.dp),

                enabled = !loading,

                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),

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
                }
            ) {

                if (loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                } else {

                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Update Password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onVisibilityChange: () -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),

        label = {
            Text(
                text = label,
                fontSize = 13.sp
            )
        },

        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },

        trailingIcon = {

            IconButton(
                onClick = onVisibilityChange
            ) {

                Icon(
                    imageVector = if (visible)
                        Icons.Default.VisibilityOff
                    else
                        Icons.Default.Visibility,
                    contentDescription = if (visible)
                        "Hide password"
                    else
                        "Show password"
                )
            }
        },

        visualTransformation =
            if (visible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),

        singleLine = true,

        shape = RoundedCornerShape(16.dp),

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun PasswordTip(
    text: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF43A047))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF4A5A4C)
        )
    }
}

