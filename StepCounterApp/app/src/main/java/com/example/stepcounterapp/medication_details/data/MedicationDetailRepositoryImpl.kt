package com.example.stepcounterapp.medication_details.data

import android.util.Log
import com.example.stepcounterapp.medication_details.domain.MedicationDetailRepository
import com.example.stepcounterapp.medication_details.domain.model.AllMedicationDetails
import com.example.stepcounterapp.medication_details.domain.model.MedicationDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MedicationDetailRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : MedicationDetailRepository {

    private val user = firebaseAuth.currentUser

    private val userId: String? = user?.uid

    private val userMedicationRef = userId?.let { userId ->
        fireStore.collection("users").document(userId)
            .collection("helpOld")
    }

    override suspend fun getMedicationDetails(): Flow<List<AllMedicationDetails>> = callbackFlow {
        val listenerRegistration = userMedicationRef?.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("medication", "Error fetching data: ${error.message}")
                close(error)  // Close the flow if there’s an error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // Convert each document to AllMedicationDetails and assign the document ID
                val medications = snapshot.documents.mapNotNull { document ->
                    val medication = document.toObject(AllMedicationDetails::class.java)
                    medication?.id = document.id  // Assign the document ID to the id field
                    medication
                }  // Filter out null results if any

                trySend(medications)  // Send the updated list to the flow
                Log.i("medication", "Data fetched successfully")
            }
        }

        // Clean up the listener when the flow collection is stopped
        awaitClose {
            listenerRegistration?.remove()
        }
    }

    override suspend fun updateMedicationDetail(id: String) {
        val medicationRef = userMedicationRef?.document(id)

        medicationRef?.get()
            ?.addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val currentIsTaken = documentSnapshot.getBoolean("taken") ?: false
                    val newIsTaken = !currentIsTaken

                    medicationRef.update("taken", newIsTaken)
                        .addOnSuccessListener {
                            Log.i("medication", "Update successful: isTaken set to $newIsTaken")
                        }
                        .addOnFailureListener { exception ->
                            Log.i("medication", "Error while updating: ${exception.message}")
                        }
                } else {
                    Log.i("medication", "Document does not exist")
                }
            }
            ?.addOnFailureListener { exception ->
                Log.i("medication", "Error fetching document: ${exception.message}")
            }
    }

}