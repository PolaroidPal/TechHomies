package com.example.stepcounterapp.details.domain.models

import com.google.firebase.firestore.PropertyName

data class NewStepTask(
    val completed: Boolean = false,

    val tracking: Boolean = false, // Use camelCase for Kotlin conventions

    @PropertyName("target_steps")
    val target_steps: Int // Use camelCase for Kotlin conventions
)
