package com.example.stepcounterapp.auth.domain

import com.example.stepcounterapp.core.domain.models.UserProfile

interface GoogleSignIn {

    suspend fun getCurrentUser() : UserProfile?

    suspend fun saveUserProfileToFireStore(userProfile: UserProfile)
}