package com.example.stepcounterapp.details.domain.models

data class StepsDetails(
    val tracking: Boolean = false,
    val completed: Boolean = false,
    val target_steps: Int = 0,
    val id: String? = null,
    val stepsCovered: Int = 0
)
