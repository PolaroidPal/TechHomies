package com.example.stepcounterapp.home.di

import com.example.stepcounterapp.home.data.HomeRepositoryImpl
import com.example.stepcounterapp.home.domain.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun providesHomeRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFireStore: FirebaseFirestore
    ): HomeRepository {
        return HomeRepositoryImpl(
            firebaseAuth = firebaseAuth,
            fireStore = firebaseFireStore
        )
    }
}