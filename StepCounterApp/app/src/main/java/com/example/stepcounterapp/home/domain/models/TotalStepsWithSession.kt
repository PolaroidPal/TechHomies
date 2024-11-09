package com.example.stepcounterapp.home.domain.models

import com.example.stepcounterapp.core.domain.models.Sessions

data class TotalStepsWithSession(
    val completed: Boolean = false,
    val target_steps: Int = 0,
    val stepsSession: List<Sessions> = emptyList()
)
