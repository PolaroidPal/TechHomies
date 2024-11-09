package com.example.stepcounterapp.settings.presentation


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stepcounterapp.settings.presentation.components.NewInfoAlert
import com.example.stepcounterapp.ui.theme.HomeColBackground

@Preview(showBackground = true)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val contact by viewModel.userName.collectAsStateWithLifecycle()

    var newContact by remember {
        mutableStateOf("")
    }

    val default = "Enter emergency contact ---->"

    var isOpen by remember {
        mutableStateOf(false)
    }

    when (isOpen) {
        true -> {
            NewInfoAlert(
                newInfo = newContact,
                onChange = {
                    newContact = it
                },
                onDismiss = {
                    isOpen = false
                },
                onConfirm = {
                    if (newContact.length < 10 || newContact.length > 10) {
                        Toast.makeText(context, "Not a valid number", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.saveData(newContact)
                        isOpen = false
                    }
                },
                title = "New Contact"
            )
        }

        else -> Unit
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeColBackground)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Emergency contact",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = contact.ifEmpty { default },
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                color = Color.White
            )

            IconButton(
                onClick = {
                    isOpen = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit contact",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                viewModel.signOutUser()
            }
        ) {
            Text(text = "Sign Out")
        }
    }
}
