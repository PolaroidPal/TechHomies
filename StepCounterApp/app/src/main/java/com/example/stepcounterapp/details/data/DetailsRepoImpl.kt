package com.example.stepcounterapp.details.data

import android.util.Log
import com.example.stepcounterapp.details.domain.DetailsRepository
import com.example.stepcounterapp.details.domain.models.NewStepTask
import com.example.stepcounterapp.details.domain.models.StepsDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DetailsRepoImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : DetailsRepository {

    private val user = firebaseAuth.currentUser

    private val userId: String? = user?.uid

    private val userStepRef = userId?.let {
        fireStore.collection("users").document(it)
            .collection("user_steps")
    }

    override suspend fun getStepsDetails(): Flow<List<StepsDetails>> = callbackFlow {
        val listenerRegistration = userStepRef?.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("userdata", "getStepsDetails: Error getting the details ${error.message}")
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                // Use a coroutine scope to handle collection of steps covered for each document
                launch {
                    val stepTasks = mutableListOf<StepsDetails>()
                    val documentChannel = Channel<StepsDetails>()

                    // Collect stepsCovered for each document in parallel and send to the channel
                    snapshot.documents.forEach { document ->
                        launch {
                            try {
                                stepsCovered(document.id).collect { stepsCovered ->
                                    val stepsDetails = document.toObject(StepsDetails::class.java)?.copy(
                                        id = document.id,
                                        stepsCovered = stepsCovered
                                    )
                                    stepsDetails?.let {
                                        documentChannel.send(it) // Send each StepsDetails to channel
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("userdata", "Error collecting steps for document ${document.id}: ${e.message}")
                            }
                        }
                    }

                    // Receive each StepsDetails from channel and accumulate
                    repeat(snapshot.documents.size) { // Ensure exact count is collected
                        stepTasks.add(documentChannel.receive())
                    }
                    // repeat method ensures that specified number of times the block inside it executes, until then execution will not go further.
                    // .receive method waits until there is something to receive.

                    trySend(stepTasks).isSuccess // Emit the completed list of StepsDetails
                    documentChannel.close() // Close channel after all documents are processed
                }
            }
        }

        // Await close to remove listener when flow collection is cancelled
        awaitClose { listenerRegistration?.remove() }
    }

    override suspend fun addNewStepTask(newStepTask: NewStepTask) {
        userStepRef?.let {
            it.add(newStepTask)
                .addOnSuccessListener {
                    Log.i("userdata", "addNewStepTask: Added data to the fireStore")
                }
                .addOnFailureListener { e ->
                    Log.i(
                        "userdata",
                        "addNewStepTask: Failed to add the data to the fireStore ${e.message}"
                    )
                    throw e
                }
        }
    }

    override suspend fun getTrackingId(): String {
        var snapshot: QuerySnapshot? = null
        userStepRef?.let {
            snapshot = it.whereEqualTo("tracking", true).limit(1).get().await()
        }

        return snapshot!!.documents[0].id
    }

    override suspend fun updateData(currentTrack: String, newTrack: String) {
        val batch = fireStore.batch()

        val docRef1 = userStepRef?.document(currentTrack)
        Log.i("update", "updateData: doc 1 ${docRef1?.id}")

        val docRef2 = userStepRef?.document(newTrack)
        Log.i("update", "updateData: doc 2 ${docRef2?.id}")

        // Add first update to the batch
        docRef1?.let {
            batch.update(docRef1, "tracking", false)
        }

        // Add second update to the batch
        docRef2?.let {
            batch.update(docRef2, "tracking", true)
        }

        // Commit the batch
        batch.commit()
            .addOnSuccessListener {
                Log.i("update", "updateData: Updated the data successfully")
            }
            .addOnFailureListener { e ->
                Log.i("update", "updateData: error updating the data ${e.message}")
            }
    }

    private suspend fun stepsCovered(id: String): Flow<Int> = callbackFlow {
        val listenerRegistration = userStepRef?.document(id)?.collection("sessions")
            ?.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Log the error and close the flow
                    Log.e("userdata", "stepsCovered: Error getting the steps ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val totalSteps = snapshot.documents.sumOf { document ->
                        document.getLong("steps")?.toInt() ?: 0
                    }
                    Log.i("userdata", "stepsCovered: Total Steps for $id: $totalSteps")
                    trySend(totalSteps).isSuccess // Emit the updated total steps
                } else {
                    trySend(0).isSuccess // Emit 0 if no documents found
                }
            }

        // Await close to remove listener when flow collection is cancelled
        awaitClose { listenerRegistration?.remove() }
    }
}