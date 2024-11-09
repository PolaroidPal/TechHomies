package com.example.stepcounterapp.core.presentation

data class LoginState(
    val isSignedIn: Boolean = false,
    val signInError: String? = null
)
