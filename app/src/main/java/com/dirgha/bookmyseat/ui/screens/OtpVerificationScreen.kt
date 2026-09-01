package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import com.dirgha.bookmyseat.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(

    navController: NavController,

    type: OtpType,

    viewModel: AuthViewModel

) {

    val email =
        viewModel.registerEmail

    var otp by remember {

        mutableStateOf(
            List(6) { "" }
        )

    }

    val focusRequesters =
        remember {
            List(6) {
                FocusRequester()
            }
        }

    var seconds by remember {
        mutableStateOf(60)
    }

    LaunchedEffect(Unit) {

        while (seconds > 0) {

            delay(1000)

            seconds--

        }

    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text("OTP Verification")

                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
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
                .padding(padding)
                .padding(horizontal = 24.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Box(

                modifier = Modifier
                    .size(90.dp)
                    .background(

                        Brush.verticalGradient(

                            listOf(
                                Color(0xFF1565C0),
                                Color(0xFF42A5F5)
                            )

                        ),

                        CircleShape

                    ),

                contentAlignment = Alignment.Center

            ) {

                Icon(

                    Icons.Default.MarkEmailRead,

                    null,

                    tint = Color.White,

                    modifier = Modifier.size(46.dp)

                )

            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(

                text = "Verify Your Email",

                fontSize = 24.sp,

                fontWeight = FontWeight.Bold

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(

                text = "Enter the 6-digit code sent to",

                color = Color.Gray

            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(

                text = email,

                fontWeight = FontWeight.Bold,

                color = MaterialTheme.colorScheme.primary

            )

            Spacer(modifier = Modifier.height(36.dp))
            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceEvenly

            ) {

                otp.forEachIndexed { index, value ->

                    OutlinedTextField(

                        value = value,

                        onValueChange = {

                            if (it.length <= 1 && it.all { c -> c.isDigit() }) {

                                val list = otp.toMutableList()

                                list[index] = it

                                otp = list

                                if (it.isNotEmpty() && index < 5) {

                                    focusRequesters[index + 1].requestFocus()

                                }

                            }

                        },

                        modifier = Modifier
                            .width(48.dp)
                            .height(60.dp)
                            .focusRequester(focusRequesters[index]),

                        singleLine = true,

                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        ),

                        shape = RoundedCornerShape(12.dp),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )

                    )

                }

            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(

                text = if (seconds > 0)
                    "Resend OTP in ${seconds}s"
                else
                    "Didn't receive the code?",

                color = Color.Gray

            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(

                enabled = seconds == 0,

                onClick = {

                    seconds = 60

                    if (type == OtpType.REGISTER) {

                        viewModel.registerSendOtp(

                            viewModel.registerName,

                            viewModel.registerPhone,

                            viewModel.registerEmail,

                            viewModel.registerPassword

                        )

                    }

                }

            ) {

                Text(

                    "Resend OTP",

                    fontWeight = FontWeight.Bold

                )

            }

            Spacer(modifier = Modifier.height(30.dp))
            Button(

                onClick = {

                    val enteredOtp = otp.joinToString("")

                    if (enteredOtp.length != 6) return@Button

                    viewModel.verifyOtp(

                        otp = enteredOtp,

                        type = type

                    ) {

                        when (type) {

                            OtpType.REGISTER -> {

                                navController.navigate(Screen.Login.route) {

                                    popUpTo(Screen.Register.route) {

                                        inclusive = true

                                    }

                                }

                            }

                            OtpType.FORGOT_PASSWORD -> {

                                navController.navigate(Screen.ResetPassword.route)

                            }

                        }}

                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(16.dp)

            ) {

                Icon(
                    Icons.Default.Email,
                    null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(

                    text = "Verify OTP",

                    fontSize = 18.sp,

                    fontWeight = FontWeight.Bold

                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(

                text =
                    if (type == OtpType.REGISTER)
                        "Complete your registration by verifying your email."
                    else
                        "Verify your OTP to continue resetting your password.",

                textAlign = TextAlign.Center,

                color = Color.Gray,

                fontSize = 13.sp

            )

        }

    }

}