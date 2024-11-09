package com.example.stepcounterapp.auth.data

import android.util.Log
import com.example.stepcounterapp.auth.domain.GoogleSignIn
import com.example.stepcounterapp.core.domain.models.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class GoogleSignInImpl @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : GoogleSignIn {

    override suspend fun saveUserProfileToFireStore(userProfile: UserProfile) {
        val userRef = firebaseFireStore.collection("users").document(userProfile.uid)

        userRef.set(userProfile)
            .addOnSuccessListener {
                Log.i(
                    "save",
                    "User data successfully written"
                )
            }
            .addOnFailureListener { exception ->
                Log.i(
                    "save",
                    "Error writing to document: $exception"
                )
            }
    }

    override suspend fun getCurrentUser(): UserProfile? {
        val currentUser = firebaseAuth.currentUser

        return currentUser?.let {
            UserProfile(
                uid = it.uid,
                displayName = it.displayName,
                email = it.email,
                photoUrl = it.photoUrl?.toString()
            )
        }
    }
}