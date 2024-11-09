package com.example.stepcounterapp.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun signOut()

    fun getContact(): Flow<String>

    suspend fun saveContact(name: String)
}