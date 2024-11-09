package com.example.stepcounterapp.step_counter.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounterapp.core.domain.models.Sessions
import com.example.stepcounterapp.step_counter.domain.StepCounterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepCounterScreenViewModel @Inject constructor(
    private val repository: StepCounterRepository
) : ViewModel(){

    private val _stepsCovered = MutableStateFlow(0)
    val stepsCovered = _stepsCovered.asStateFlow()

    private var distanceCovered = mutableStateOf("")

    private var isTracking = false

    fun startStepCounter(contact: String) {
        if(!isTracking) {
            viewModelScope.launch(Dispatchers.Default) {
                val stepCounterJob = async {
                    repository.startTrackingSteps {
                        _stepsCovered.value = it
                    }
                }

                val fallDetectorJob = async {
                    repository.startProximitySensor(contact)
                }

                stepCounterJob.await()
                fallDetectorJob.await()
            }
            isTracking = true
        }
    }

    fun stopTracking() {
        if(isTracking) {
            repository.stopSensor()
            distanceCovered.value = calculateDistance(_stepsCovered.value).toString()
            isTracking = false
        }
    }

    fun saveSession(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUserSession(
                Sessions(
                    kilometers = distanceCovered.value,
                    steps = _stepsCovered.value
                ),
                id = id
            )
            _stepsCovered.value = 0
        }
    }

    private fun calculateDistance(steps: Int, strideLengthInMeters: Double = 0.75) : Double {
        return (steps * strideLengthInMeters) / 1000
    }
}