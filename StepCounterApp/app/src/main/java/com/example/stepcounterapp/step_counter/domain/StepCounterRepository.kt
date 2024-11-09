package com.example.stepcounterapp.step_counter.domain

import com.example.stepcounterapp.core.domain.models.Sessions
import kotlinx.coroutines.flow.StateFlow

interface StepCounterRepository {

    val fallDetected: StateFlow<Boolean>

    suspend fun saveUserSession(userSession: Sessions, id: String)

    suspend fun startTrackingSteps(onStepDetected: (Int) -> Unit)

    fun stopSensor()

    suspend fun startProximitySensor(urgentContact: String)
}