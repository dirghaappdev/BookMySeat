package com.dirgha.bookmyseat.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun UpdateDialog(
    title: String,
    message: String,
    forceUpdate: Boolean,
    onUpdate: () -> Unit,
    onLater: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            if (!forceUpdate) {
                onLater()
            }
        },

        title = {
            Text(text = title)
        },

        text = {
            Text(text = message)
        },

        confirmButton = {

            Button(
                onClick = onUpdate
            ) {
                Text("Update")
            }

        },

        dismissButton = {

            if (!forceUpdate) {

                TextButton(
                    onClick = onLater
                ) {
                    Text("Later")
                }

            }

        }
    )

}