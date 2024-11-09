package com.example.stepcounterapp.medication_details.presentation.screens

import com.google.firebase.Timestamp

sealed class AddMedicationScreenEvent {

    data class OnNameChange(val name: String) : AddMedicationScreenEvent()

    data class OnTimeStampChange(val time: Timestamp) : AddMedicationScreenEvent()

    data object OnSubmit : AddMedicationScreenEvent()
}