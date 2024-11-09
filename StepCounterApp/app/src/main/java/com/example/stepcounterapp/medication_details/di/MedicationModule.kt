package com.example.stepcounterapp.medication_details.di

import com.example.stepcounterapp.medication_details.data.AddMedicationDetailsRepositoryImpl
import com.example.stepcounterapp.medication_details.data.MedicationDetailRepositoryImpl
import com.example.stepcounterapp.medication_details.domain.AddMedicationDetailsRepository
import com.example.stepcounterapp.medication_details.domain.MedicationDetailRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MedicationModule {

    @Provides
    @Singleton
    fun provideAddMedicationDetailsRepository(
        firebaseAuth: FirebaseAuth,
        firebaseStore: FirebaseFirestore
    ) : AddMedicationDetailsRepository {
        return AddMedicationDetailsRepositoryImpl(
            firebaseAuth = firebaseAuth,
            firebaseFireStore = firebaseStore
        )
    }

    @Provides
    @Singleton
    fun provideMedicationDetailRepository(
        firebaseAuth: FirebaseAuth,
        firebaseStore: FirebaseFirestore
    ) : MedicationDetailRepository {
        return MedicationDetailRepositoryImpl(
            firebaseAuth = firebaseAuth,
            fireStore = firebaseStore
        )
    }
}