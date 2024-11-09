package com.example.stepcounterapp.details.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounterapp.details.domain.DetailsRepository
import com.example.stepcounterapp.details.domain.models.NewStepTask
import com.example.stepcounterapp.details.domain.models.StepsDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailsRepository
): ViewModel(){

    val id = mutableStateOf("")

    private val _stepsDetails = MutableStateFlow(listOf(StepsDetails()))
    val stepsDetails = _stepsDetails.onStart {
        loadInitialData()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private fun loadInitialData() {
        viewModelScope.launch {
            getStepsDetails()
            Log.i("userdata", "getStepsTracker: ${_stepsDetails.value}")
        }

        viewModelScope.launch {
            id.value = repository.getTrackingId()
        }
    }

    fun addNewTask(newStepTask: NewStepTask) {
        viewModelScope.launch {
            addNewStepTask(newStepTask)
        }
    }

    fun updateTracker(currentTrack: String, newTrack: String) {
        viewModelScope.launch {
            updateData(
                currentTrack = currentTrack,
                newTrack = newTrack
            )
        }
    }

    private suspend fun updateData(currentTrack: String, newTrack: String) {
        withContext(Dispatchers.IO) {
            repository.updateData(
                currentTrack = currentTrack,
                newTrack = newTrack
            )
        }
    }

    private suspend fun getStepsDetails() {
        withContext(Dispatchers.IO) {
            repository.getStepsDetails().collect { details ->
                _stepsDetails.value = details
            }
        }
    }

    private suspend fun addNewStepTask(newStepTask: NewStepTask) {
        withContext(Dispatchers.IO) {
            repository.addNewStepTask(newStepTask)
        }
    }
}