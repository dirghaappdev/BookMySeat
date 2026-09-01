package com.dirgha.bookmyseat.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {



    val context = androidx.compose.ui.platform.LocalContext.current

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }

    val message by viewModel.message.collectAsState()
    LaunchedEffect(Unit) {
        Log.d("EMAIL_TEST", "Reset Screen = ${viewModel.registerEmail}")
    }
    LaunchedEffect(message) {

        if (message.isBlank()) return@LaunchedEffect

        loading = false

        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()

        if (message.contains("success", true)) {

            navController.navigate(Screen.Login.route) {

                popUpTo(0)

            }

        }

        viewModel.clearMessage()

    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Reset Password")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
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
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Icon(

                Icons.Default.LockReset,

                null,

                modifier = Modifier.size(70.dp),

                tint = MaterialTheme.colorScheme.primary

            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(

                "Create New Password",

                fontSize = 24.sp,

                fontWeight = FontWeight.Bold

            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(

                "Your OTP has been verified.\nPlease create a new password.",

                textAlign = androidx.compose.ui.text.style.TextAlign.Center

            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(

                value = newPassword,

                onValueChange = {
                    newPassword = it
                },

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("New Password")
                },

                leadingIcon = {

                    Icon(
                        Icons.Default.LockReset,
                        null
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

                            null

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

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(

                value = confirmPassword,

                onValueChange = {
                    confirmPassword = it
                },

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("Confirm Password")
                },

                leadingIcon = {

                    Icon(
                        Icons.Default.LockReset,
                        null
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

                            null

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

            Spacer(modifier = Modifier.height(32.dp))

            Button(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),

                enabled = !loading,

                shape = RoundedCornerShape(14.dp),

                onClick = {
                    Log.d("CLICK", "Reset button clicked")

                    when {

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

                            viewModel.resetPassword(
                                newPassword
                            ) {}

                        }

                    }

                }

            ) {

                if (loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        "Reset Password"
                    )

                }

            }

        }

    }

}