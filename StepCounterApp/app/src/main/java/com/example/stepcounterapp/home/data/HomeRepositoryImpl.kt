package com.example.stepcounterapp.home.data

import android.util.Log
import com.example.stepcounterapp.core.domain.models.Sessions
import com.example.stepcounterapp.core.domain.models.UserProfile
import com.example.stepcounterapp.home.domain.HomeRepository
import com.example.stepcounterapp.home.domain.models.StepsProgress
import com.example.stepcounterapp.home.domain.models.TotalStepsWithSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CancellationException

class HomeRepositoryImpl(
    firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : HomeRepository {

    private val user = firebaseAuth.currentUser

    private val userId: String? = user?.uid

    private val userStepsRef = userId?.let { userId ->
        fireStore.collection("users").document(userId)
            .collection("user_steps")
    }

    override suspend fun getUser(): UserProfile? {
        return user?.let {
            UserProfile(
                uid = it.uid,
                displayName = it.displayName,
                email = it.email,
                photoUrl = it.photoUrl?.toString()
            )
        }
    }

    override suspend fun getTotalStepsWithSession(): Flow<TotalStepsWithSession?> = flow {
        userStepsRef?.let {
            try {
                // Fetch the first document where isTracking is true
                val snapshot = it.whereEqualTo("tracking", true).limit(1).get().await()

                if (!snapshot.isEmpty) {
                    val document = snapshot.documents[0]
                    // Convert the FireStore document to a TotalStepsWithSession object

                    val totalSteps = document.toObject(TotalStepsWithSession::class.java)


                    // Fetch sessions and combine with totalSteps
                    val sessionsFlow = getSessions(document.id)

                    sessionsFlow.collect { sessions ->
                        // Emit a new instance of TotalStepsWithSession with updated stepsSession
                        totalSteps?.let { totalStepsWithSessions ->
                            // Create a new TotalStepsWithSession instance with updated stepsSession
                            val updatedTotalSteps = totalStepsWithSessions.copy(stepsSession = sessions)
                            emit(updatedTotalSteps) // Emit the updated instance
                        }
                    }
                } else {
                    emit(null)  // Emit null if no documents found
                }
            } catch (e: Exception) {
                emit(null)  // Handle error and emit null
            }
        } ?: emit(null) // Emit null if userStepsRef is null
    }

    override fun getSessions(totalStepsId: String): Flow<List<Sessions>> = flow {
        userStepsRef?.let {
            try {
                val snapShot = it.document(totalStepsId)
                    .collection("sessions")
                    .get()
                    .await()

//                Log.i("initial", "repo: in get sessions function $snapShot")
//
//                if (snapShot.isEmpty) {
//                    Log.i("initial", "No documents found in the sessions collection.")
//                } else {
//                    Log.i("initial", "Documents found in the sessions collection: ${snapShot.size()} documents")
//                }
//
//                snapShot.documents.forEach { document ->
//                    Log.i("initial", "Document data: ${document.data}")
//                }

                val sessions = snapShot.toObjects<Sessions>()


                emit(sessions)
            } catch (e: Exception) {
                Log.i("initial", "getSessions: ${e.message}")
                emit(emptyList())
            }
        }
    }

    override suspend fun getTotalStepsId(): String {
        var snapshot: QuerySnapshot? = null
        userStepsRef?.let {
            snapshot = it.whereEqualTo("tracking", true).limit(1).get().await()
        }

        return snapshot!!.documents[0].id
    }

    override suspend fun getStepsProgress(): StepsProgress {
        var totalKilometers = 0.0
        var totalSteps = 0

        try {
            val id = getTotalStepsId()

            userStepsRef?.let {
                val snapshot = it.document(id)
                    .collection("sessions")
                    .get()
                    .await()

                val sessions = snapshot.toObjects<Sessions>()

                totalSteps = sessions.sumOf {  session ->
                    session.steps
                }

                totalKilometers = sessions.sumOf { session ->
                    session.kilometers.toDouble()
                }
            }
        } catch (e: CancellationException) {
            Log.i("stepsProgress", "getStepsProgress: ${e.message}")
            e.printStackTrace()
        } catch (e: Exception) {
            Log.i("stepsProgress", "getStepsProgress: ${e.message}")
            e.printStackTrace()
        }

        return StepsProgress(
            kilometerCovered = totalKilometers.toString(),
            stepsCovered = totalSteps
        )
    }

    override suspend fun updateData(completed: Boolean?) {
        val id = getTotalStepsId()

        val updates = mutableMapOf<String, Any>()

        completed?.let {
            updates["completed"] = it
        }

        userStepsRef?.let {
            it.document(id).update(updates)
                .addOnSuccessListener {
                    println("Successfully updated the document")
                }
                .addOnFailureListener { e ->
                    println("Error updating document ${e.message}")
                }
        }
    }
}