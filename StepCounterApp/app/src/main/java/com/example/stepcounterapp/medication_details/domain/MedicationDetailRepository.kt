package com.example.stepcounterapp.medication_details.domain

import com.example.stepcounterapp.medication_details.domain.model.AllMedicationDetails
import kotlinx.coroutines.flow.Flow

interface MedicationDetailRepository {

    suspend fun getMedicationDetails() : Flow<List<AllMedicationDetails>>

    suspend fun updateMedicationDetail(id: String)
}