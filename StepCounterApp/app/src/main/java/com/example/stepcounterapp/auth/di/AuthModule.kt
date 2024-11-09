package com.example.stepcounterapp.auth.di

import com.example.stepcounterapp.auth.data.GoogleSignInImpl
import com.example.stepcounterapp.auth.domain.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideGoogleSignInRepository(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : GoogleSignIn {
        return GoogleSignInImpl(fireStore, firebaseAuth)
    }
}