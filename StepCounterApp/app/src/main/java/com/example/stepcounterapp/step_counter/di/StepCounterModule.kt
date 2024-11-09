package com.example.stepcounterapp.step_counter.di

import android.content.Context
import android.hardware.SensorManager
import com.example.stepcounterapp.step_counter.data.StepCounterRepositoryImpl
import com.example.stepcounterapp.step_counter.domain.StepCounterRepository
import com.example.stepcounterapp.step_counter.domain.use_cases.StepDetectorUseCase
import com.example.stepcounterapp.step_counter.domain.use_cases.TrackStepsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StepCounterModule {

    @Provides
    @Singleton
    fun providesSensorManager(@ApplicationContext context: Context) : SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @Provides
    @Singleton
    fun providesStepCounterRepository(
        firebaseAuth: FirebaseAuth,
        sensorManager: SensorManager,
        fireStore: FirebaseFirestore,
        trackStepsUseCase: TrackStepsUseCase
    ) : StepCounterRepository {
        return StepCounterRepositoryImpl(firebaseAuth, sensorManager, fireStore, trackStepsUseCase)
    }

    @Provides
    @Singleton
    fun providesStepDetectorUseCase() : StepDetectorUseCase {
        return StepDetectorUseCase()
    }

    @Provides
    @Singleton
    fun providesTrackStepsUseCase(stepDetectorUseCase: StepDetectorUseCase) : TrackStepsUseCase {
        return TrackStepsUseCase(stepDetectorUseCase)
    }
}