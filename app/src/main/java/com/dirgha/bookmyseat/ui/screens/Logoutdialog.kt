package com.dirgha.bookmyseat.ui.screens


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun LogoutDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Logout")
            },
            text = {
                Text(
                    "Are you sure you want to logout?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(
                        "Logout",
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}