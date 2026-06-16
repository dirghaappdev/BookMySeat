package com.dirgha.bookmyseat.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dirgha.bookmyseat.data.local.SessionManager
import com.dirgha.bookmyseat.data.local.ThemeMode

@Composable
fun SettingsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "App Theme",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = {
                SessionManager.themeMode =
                    ThemeMode.LIGHT
            }
        ) {
            Text("Light")
        }

        Button(
            onClick = {
                SessionManager.themeMode =
                    ThemeMode.DARK
            }
        ) {
            Text("Dark")
        }

        Button(
            onClick = {
                SessionManager.themeMode =
                    ThemeMode.SYSTEM
            }
        ) {
            Text("System")
        }
    }
}