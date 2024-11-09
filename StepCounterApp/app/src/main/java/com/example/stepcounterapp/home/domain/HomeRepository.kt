package com.example.stepcounterapp.home.domain

import com.example.stepcounterapp.core.domain.models.UserProfile
import com.example.stepcounterapp.core.domain.models.Sessions
import com.example.stepcounterapp.home.domain.models.StepsProgress
import com.example.stepcounterapp.home.domain.models.TotalStepsWithSession
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getUser() : UserProfile?

    suspend fun getTotalStepsWithSession() : Flow<TotalStepsWithSession?>

    fun getSessions(totalStepsId: String) : Flow<List<Sessions>>

    suspend fun getTotalStepsId() : String

    suspend fun getStepsProgress() : StepsProgress

    suspend fun updateData(completed: Boolean?)
}