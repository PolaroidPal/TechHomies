package com.example.stepcounterapp.medication_details.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounterapp.medication_details.domain.MedicationDetailRepository
import com.example.stepcounterapp.medication_details.domain.model.AllMedicationDetails
import com.example.stepcounterapp.medication_details.domain.model.MedicationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    private val repository: MedicationDetailRepository
) : ViewModel() {

    private val _medicationDetails = MutableStateFlow(listOf(AllMedicationDetails()))
    val medicationDetails = _medicationDetails
        .onStart { getInitialData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    fun update(id: String) {
        viewModelScope.launch {
            updateData(id)
        }
    }

    private fun getInitialData()  {
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        withContext(Dispatchers.IO) {
            repository.getMedicationDetails().collect {
                _medicationDetails.value = it
            }
        }
    }

    private suspend fun updateData(id: String) {
        withContext(Dispatchers.IO) {
            repository.updateMedicationDetail(id)
        }
    }
}