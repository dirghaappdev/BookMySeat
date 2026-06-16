package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dirgha.bookmyseat.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {

        delay(2000)

        navController.navigate(
            Screen.Login.route
        ) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "🚖",
            style =
                MaterialTheme.typography.displayLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "BookMySeat",
            style =
                MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Intercity Shared Travel"
        )
    }
}