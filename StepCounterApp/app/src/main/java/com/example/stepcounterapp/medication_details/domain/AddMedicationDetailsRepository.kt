package com.example.stepcounterapp.medication_details.domain

import com.example.stepcounterapp.medication_details.domain.model.MedicationDetails

interface AddMedicationDetailsRepository {

    suspend fun addMedicationDetails(medicationDetails: MedicationDetails)
}