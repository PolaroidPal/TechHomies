package com.example.stepcounterapp.medication_details.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stepcounterapp.medication_details.presentation.components.TimePickerDialog
import com.example.stepcounterapp.medication_details.presentation.viewmodels.AddMedicationViewModel
import com.example.stepcounterapp.ui.theme.HomeColBackground
import com.example.stepcounterapp.ui.theme.HomeRowBackground
import com.google.firebase.Timestamp
import java.util.Calendar

val mainModRow = Modifier
    .background(HomeRowBackground)
    .padding(start = 18.dp)
    .fillMaxSize()

val mainModCol = Modifier
    .background(HomeColBackground)
    .padding(start = 18.dp)
    .fillMaxSize()

@Composable
fun AddMediationScreen(
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {

    when (windowSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CompactAddMedicationScreen()
        }

        else -> {
            CompactAddMedicationScreen()
        }
    }
}

@Preview(showBackground = true, name = "Landscape", widthDp = 853, heightDp = 384)
@Composable
private fun AddMedicationScreenPreview() {
    AddMediationScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Portrait", widthDp = 384, heightDp = 853)
@Composable
fun CompactAddMedicationScreen(
    viewModel: AddMedicationViewModel = hiltViewModel()
) {

    var isOpen by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = mainModCol,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TextField(
                value = viewModel.name,
                onValueChange = {
                    viewModel.onEvent(AddMedicationScreenEvent.OnNameChange(it))
                },
                placeholder = { Text(text = "Enter the name of the medicine")},
                modifier = Modifier
                    .padding(20.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pick the time for the medicine",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                )

                IconButton(onClick = { isOpen = true}) {
                    Icon(imageVector = Icons.Default.Timer, contentDescription = "")
                }
            }

            val currentTime = Calendar.getInstance()

            val timePickerState = rememberTimePickerState(
                initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                initialMinute = currentTime.get(Calendar.MINUTE),
                is24Hour = true,
            )

            if (isOpen) {
                TimePickerDialog(
                    onDismiss = { isOpen = false },
                    onConfirm = { hour, minute ->
                        // Access the selected hour and minute from timePickerState
                        val selectedHour = timePickerState.hour
                        val selectedMinute = timePickerState.minute

                        // Perform timestamp conversion
                        val selectedCalendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, selectedHour)
                            set(Calendar.MINUTE, selectedMinute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val timestamp = selectedCalendar.timeInMillis
                        viewModel.onEvent(AddMedicationScreenEvent.OnTimeStampChange(convertLongToTimestamp(timestamp)))

                        println("Selected timestamp: $timestamp")
                        viewModel.onEvent(AddMedicationScreenEvent.OnSubmit)
                        isOpen = false
                    }
                ) {
                    TimePicker(
                        state = timePickerState,
                    )
                }
            }
        }
    }
}

fun convertLongToTimestamp(timeInMillis: Long): Timestamp {
    return Timestamp(timeInMillis / 1000, ((timeInMillis % 1000) * 1_000_000).toInt())
}