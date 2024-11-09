package com.example.stepcounterapp.details.domain

import com.example.stepcounterapp.details.domain.models.NewStepTask
import com.example.stepcounterapp.details.domain.models.StepsDetails
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    suspend fun getStepsDetails() : Flow<List<StepsDetails>>

    suspend fun addNewStepTask(newStepTask: NewStepTask)

    suspend fun getTrackingId(): String

    suspend fun updateData(currentTrack: String, newTrack: String)
}