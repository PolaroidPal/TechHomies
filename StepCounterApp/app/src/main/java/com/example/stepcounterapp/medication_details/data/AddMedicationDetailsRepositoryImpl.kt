package com.example.stepcounterapp.medication_details.data

import android.util.Log
import com.example.stepcounterapp.medication_details.domain.AddMedicationDetailsRepository
import com.example.stepcounterapp.medication_details.domain.model.MedicationDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddMedicationDetailsRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) : AddMedicationDetailsRepository {

    private val user = firebaseAuth.currentUser

    private val userId: String? = user?.uid

    private val userMedicationRef = userId?.let { userId ->
        firebaseFireStore.collection("users").document(userId)
            .collection("helpOld")
    }

    override suspend fun addMedicationDetails(medicationDetails: MedicationDetails) {
        userMedicationRef?.let { ref ->
            ref.add(medicationDetails)
                .addOnSuccessListener {
                    Log.i("medication", "addMedicationDetails: Adding medication is successful")
                }
                .addOnFailureListener {
                    Log.i("medication", "addMedicationDetails: Adding data is unsuccessful")
                }
        }
    }
}