package com.example.stepcounterapp.medication_details.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounterapp.medication_details.domain.AddMedicationDetailsRepository
import com.example.stepcounterapp.medication_details.domain.model.MedicationDetails
import com.example.stepcounterapp.medication_details.presentation.screens.AddMedicationScreenEvent
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val repository: AddMedicationDetailsRepository
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var timeStamp by mutableStateOf(Timestamp.now())
        private set

    fun onEvent(event: AddMedicationScreenEvent) {
        when (event) {
            is AddMedicationScreenEvent.OnNameChange ->  {
                name = event.name
            }

            is AddMedicationScreenEvent.OnTimeStampChange -> {
                timeStamp = event.time
            }

            AddMedicationScreenEvent.OnSubmit -> {
                if (name.isNotEmpty()) {
                    val medicationDetails = MedicationDetails(
                        isTaken = false,
                        medicationName = name,
                        medicationTime = timeStamp
                    )

                    viewModelScope.launch {
                        addMedicationDetails(medicationDetails)
                    }
                }
            }
        }
    }

    private suspend fun addMedicationDetails(medicationDetails: MedicationDetails) {
        withContext(Dispatchers.IO) {
            repository.addMedicationDetails(medicationDetails)
        }
    }
}