package com.dirgha.bookmyseat.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.viewmodel.AuthViewModel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.utils.DeviceUtils
import com.dirgha.bookmyseat.utils.InternetManager
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {

    var phone by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val message by viewModel.message.collectAsState()
    var displayMessage by remember { mutableStateOf("") }

    LaunchedEffect(displayMessage) {
        if (message.isNotEmpty()) {
            delay(3000)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(message) {
        displayMessage =
            if (
                message.isNotBlank() &&
                !message.contains("success", ignoreCase = true)
            ) {
                message
            } else {
                ""
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            displayMessage = ""
            viewModel.clearMessage()
        }
    }

    val fieldShape = RoundedCornerShape(14.dp)
    val fieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF1F2F4),
        focusedContainerColor = Color(0xFFF1F2F4),
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEAF1FE),
                        Color.White
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // ---------- Brand ----------
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "DailyCabs",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ---------- Heading ----------
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ---------- Phone ----------
            TextField(
                value = phone,
                onValueChange = {
                    if (it.all(Char::isDigit) && it.length <= 10) {
                        phone = it
                    }
                },
                placeholder = {
                    Text("Mobile number")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                shape = fieldShape,
                colors = fieldColors,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ---------- Password ----------
            //var password by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }

            TextField(
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text("Password")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                singleLine = true,
                shape = fieldShape,
                colors = fieldColors,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Text(

                    text = "Forgot Password?",

                    color = MaterialTheme.colorScheme.primary,

                    fontWeight = FontWeight.SemiBold,

                    modifier = Modifier.clickable {

                        displayMessage = ""

                        viewModel.clearMessage()

                        navController.navigate(
                            Screen.ForgotPassword.route
                        )

                    }

                )

            }

            if (displayMessage.isNotEmpty()) {

                Spacer(modifier = Modifier.height(6.dp))

                Text(

                    text = displayMessage,

                    color = MaterialTheme.colorScheme.error,

                    style = MaterialTheme.typography.bodySmall

                )

            }

            Spacer(modifier = Modifier.height(24.dp))
            val context = LocalContext.current            // ---------- Sign In ----------
            Button(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = phone.length == 10 &&
                        password.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0B0C14),
                    contentColor = Color.White
                ),
                onClick = {
                    val deviceId = DeviceUtils.getDeviceId(context)
                    if (!InternetManager.requireInternet(context)) {

                        Toast.makeText(
                            context,
                            "No Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    viewModel.login(
                        phone = phone,
                        password = password,
                        deviceId = deviceId,
                    ) { role ->
                        SessionManager.save(context)
                        displayMessage = ""
                        viewModel.clearMessage()
                        if (role == "ADMIN") {

                            navController.navigate("admin_home") {
                                popUpTo(Screen.Login.route) {
                                    inclusive = true
                                }
                            }

                        } else {

                            navController.navigate("member_home") {
                                popUpTo(Screen.Login.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = "Sign In",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---------- Sign up ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.Gray
                )
                Text(
                    text = "Sign up",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        displayMessage = ""
                        viewModel.clearMessage()
                        navController.navigate(
                            Screen.Register.route
                        )
                    }
                )
            }
        }
    }
}