package com.dirgha.bookmyseat.ui.screens

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {

    var name by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val message by
    viewModel.message.collectAsState()

    val context = LocalContext.current

    // What we actually show inline. Mirrors `message`, but gets wiped the
    // moment this screen leaves composition (back/sign-in/navigation),
    // so a stale message never reappears later.
    var displayMessage by remember { mutableStateOf("") }

    // Local, client-side validation error (10-digit phone, 3-6 char password)
    var validationError by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            if (message.contains("success", ignoreCase = true)) {
                // Success -> toast instead of a lingering on-screen message.
                val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP, 0, 150) // swap to Gravity.BOTTOM if preferred
                toast.show()
                displayMessage = ""
            } else {
                displayMessage = message
            }
        }
    }

    // Screen is leaving composition (navigated away from) -> clear it.
    DisposableEffect(Unit) {
        onDispose {
            displayMessage = ""
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

        // ---------- Back arrow: pinned to the top, independent of the
        // centered block below ----------
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 24.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

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
                    text = "Book My Seat",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ---------- Heading ----------
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sign up to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ---------- Name ----------
            TextField(
                value = name,
                onValueChange = {
                    name = it
                },
                placeholder = {
                    Text("Full name")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                shape = fieldShape,
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ---------- Phone: digits only, capped at 10 ----------
            TextField(
                value = phone,
                onValueChange = {
                    phone = it.filter { c -> c.isDigit() }.take(10)
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

            // ---------- Password: capped at 6 (min of 3 enforced on submit) ----------
            TextField(
                value = password,
                onValueChange = {
                    password = it.take(6)
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
                singleLine = true,
                shape = fieldShape,
                colors = fieldColors,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (validationError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = validationError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else if (displayMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = displayMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ---------- Create Account ----------
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0B0C14),
                    contentColor = Color.White
                ),
                onClick = {

                    when {
                        phone.length != 10 -> {
                            validationError = "Mobile number must be exactly 10 digits"
                        }
                        password.length < 3 || password.length > 6 -> {
                            validationError = "Password must be between 3 and 6 characters"
                        }
                        else -> {
                            validationError = ""

                            viewModel.register(
                                name,
                                phone,
                                password
                            )
                        }
                    }
                }
            ) {
                Text(
                    text = "Create Account",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---------- Sign in ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color.Gray
                )
                Text(
                    text = "Sign in",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}