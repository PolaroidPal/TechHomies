package com.example.stepcounterapp.core.domain.models

import androidx.compose.runtime.Stable

@Stable
data class UserProfile(
    val uid: String = "",
    val displayName: String? = "Loading",
    val email: String? = "Loading",
    val photoUrl: String? = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4YreOWfDX3kK-QLAbAL4ufCPc84ol2MA8Xg&s"
)
