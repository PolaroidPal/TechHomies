package com.example.stepcounterapp.settings.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun NewInfoAlert(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    newInfo: String = "",
    onChange: (String) -> Unit = {},
    title: String = "New"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            TextField(
                value = newInfo,
                onValueChange = {
                    onChange(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    )
}