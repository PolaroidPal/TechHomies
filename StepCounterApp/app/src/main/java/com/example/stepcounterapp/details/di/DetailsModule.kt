package com.example.stepcounterapp.details.di

import com.example.stepcounterapp.details.data.DetailsRepoImpl
import com.example.stepcounterapp.details.domain.DetailsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailsModule {

    @Provides
    @Singleton
    fun providesDetailsRepository(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ) : DetailsRepository {
        return DetailsRepoImpl(
            firebaseAuth, fireStore
        )
    }
}