package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Generic, reusable success screen. Drop this in after ANY flow that ends in
 * a positive outcome (trip created, booking confirmed, payment done, etc.)
 * by just passing a message and the button action(s) you want.
 */
@Composable
fun SuccessScreen(
    message: String,
    title: String = "Success",
    primaryButtonText: String = "Done",
    onPrimaryClick: () -> Unit,
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(150.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onPrimaryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(primaryButtonText)
            }

            if (secondaryButtonText != null && onSecondaryClick != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onSecondaryClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(secondaryButtonText)
                }
            }
        }
    }
}

/**
 * Thin wrapper that wires the generic SuccessScreen into the "trip created"
 * flow specifically. Plug this into your NavGraph for the trip_success route.
 * Replace "dashboard" below with whatever your real home/dashboard route is named.
 */
@Composable
fun TripSuccessScreen(navController: NavController, message: String) {
    SuccessScreen(
        message = message,
        primaryButtonText = "Back to Dashboard",
        onPrimaryClick = {
            navController.popBackStack(route = "admin_home", inclusive = false)
        },
        secondaryButtonText = "Create Another Trip",
        onSecondaryClick = {
            navController.popBackStack()
        }
    )
}
