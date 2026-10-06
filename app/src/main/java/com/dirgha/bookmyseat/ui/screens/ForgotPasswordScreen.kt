package com.dirgha.bookmyseat.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.data.config.SupportConfig
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.utils.ConfigManager
import com.dirgha.bookmyseat.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(

    navController: NavController,

    viewModel: AuthViewModel

) {

    val context = LocalContext.current

    var email by remember {

        mutableStateOf("")

    }


    var supportConfig by remember {
        mutableStateOf<SupportConfig?>(null)
    }

    LaunchedEffect(Unit) {

        viewModel.clearMessage()

        val response = ConfigManager(context).getConfiguration()

        supportConfig = response?.data?.config?.support
    }
    BackHandler {

        viewModel.clearMessage()

        navController.popBackStack()
    }
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:${supportConfig?.email}")

        putExtra(
            Intent.EXTRA_SUBJECT,
            "Forgot Registered Email - DailyCabs"
        )

        putExtra(
            Intent.EXTRA_TEXT,
            """
Hello ${supportConfig?.name},

I am unable to reset my password because I don't remember the email address used during registration.

My details are:

Name:
Registered Mobile Number:
Approximate Registration Date:

Please help me recover my account.

Thank you.
        """.trimIndent()
        )
    }

   // context.startActivity(intent)


    val message by viewModel.message.collectAsState()

    LaunchedEffect(message) {

        if (message.isBlank()) return@LaunchedEffect

        if (message == "OTP sent successfully") {

            viewModel.registerEmail = email

            navController.navigate(
                Screen.OtpVerification.createRoute(
                    OtpType.FORGOT_PASSWORD.name
                )
            )

        }
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text("Forgot Password")

                },
                navigationIcon = {

                    IconButton(
                        onClick = {

                            viewModel.clearMessage()

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
                .padding(padding)
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Icon(

                Icons.Default.LockReset,

                null,

                modifier = Modifier.size(80.dp)

            )

            Spacer(Modifier.height(20.dp))

            Text(

                "Enter your registered email"

            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(

                value = email,

                onValueChange = {

                    email = it

                },

                leadingIcon = {

                    Icon(
                        Icons.Default.Email,
                        null
                    )

                },

                keyboardOptions = KeyboardOptions(

                    keyboardType = KeyboardType.Email

                ),

                modifier = Modifier.fillMaxWidth()

            )

            Spacer(Modifier.height(30.dp))
            Log.d("EMAIL_TEST", "Before Navigate = ${viewModel.registerEmail}")
            Button(

                modifier = Modifier.fillMaxWidth(),

                onClick = {

                    viewModel.sendForgotPasswordOtp(

                        email

                    )

                }

            ) {

                Text("Send OTP")

            }
            if (message.isNotBlank()) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    color = if (message == "OTP sent successfully") {
                        Color(0xFF2E7D32)
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Need Help?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "If you don't remember the email address you registered with, or you no longer have access to it, please contact our support team.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = supportConfig?.email ?: "",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            context.startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Please include your registered mobile number and your full name so we can verify your account and assist you.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

        }

    }

}