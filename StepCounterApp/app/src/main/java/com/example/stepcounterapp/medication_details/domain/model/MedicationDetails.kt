package com.example.stepcounterapp.medication_details.domain.model

import com.google.firebase.Timestamp

data class MedicationDetails(
    val isTaken: Boolean = false,
    val medicationName: String = "",
    val medicationTime: Timestamp = Timestamp.now()
)
