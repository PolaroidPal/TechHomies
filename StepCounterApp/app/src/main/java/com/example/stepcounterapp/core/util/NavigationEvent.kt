package com.example.stepcounterapp.core.util

import kotlinx.serialization.Serializable

sealed class NavigationEvent {

    @Serializable
    data object LoginScreen: NavigationEvent()

    @Serializable
    data object HomeScreen: NavigationEvent()

    @Serializable
    data class StepCounterScreen(val id: String): NavigationEvent()

    @Serializable
    data object DetailsScreen: NavigationEvent()

    @Serializable
    data object SettingsScreen: NavigationEvent()

    @Serializable
    data object AddMedicationDetailScreen : NavigationEvent()

    @Serializable
    data object MedicationDetailsScreen : NavigationEvent()
}