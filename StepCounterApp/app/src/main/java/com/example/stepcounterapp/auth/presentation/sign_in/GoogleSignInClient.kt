package com.example.stepcounterapp.auth.presentation.sign_in

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.stepcounterapp.R
import com.example.stepcounterapp.auth.domain.GoogleSignIn
import com.example.stepcounterapp.core.domain.models.UserProfile
import com.example.stepcounterapp.core.presentation.LoginState
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

class GoogleSignInClient (
    private val repository: GoogleSignIn,
    private val context: Activity
) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()



    fun handleSignIn() {
        scope.launch {
            Log.i("googleSignIn", "handleGoogleSignIn: starting sign in process")
            googleSignIn().collect { result ->
                result.fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isSignedIn = true
                            )
                        }

                        try {
                            val currentUser = getUserData()

                            currentUser?.let {
                                saveUserProfile(currentUser)
                            }
                        } catch (e : Exception) {
                            println("Error: ${e.message}")
                        }
                    },
                    onFailure = {
                        val error = it.message ?: "An unknown error occurred"
                        _state.update {
                            it.copy(
                                signInError = error
                            )
                        }
                    }
                )
            }
        }
    }

    private suspend fun googleSignIn() : Flow<Result<AuthResult>> {
        val credentialManager = CredentialManager.create(context)
        return callbackFlow {
            try {
                Log.i("googleSignIn", "Sign in started")
                // Initialize the credential manager


                // Generate a nonce (a random number generated once)
                val nonce = UUID.randomUUID().toString()
                val hashedNonce = hashNonce(nonce)

                Log.i("googleSignIn", "Nonce created")

                // Set up google ID option
                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setNonce(hashedNonce)
                    .build()

                Log.i("googleSignIn", "Google option is created")

                // Request credentials
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                Log.i("googleSignIn", "Request is created")

                // Get the credential result
                Log.i("googleSignIn", "Attempting to get credential...")
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                Log.i("googleSignIn", "Credential retrieved: ${result.credential}")

                Log.i("googleSignIn", "Credential is created")

                // Check if the received credential is a valid Google ID Token.
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                    // Sign in to Firebase with Google Credentials
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                    // Send result
                    trySend(Result.success(authResult))
                } else {
                    Log.i("googleSignIn", "Received an invalid credential type")
                    throw RuntimeException("Received an invalid credential type")
                }
            } catch (e: GetCredentialCancellationException) {
                Log.i("googleSignIn", "Sign in was cancelled")
                trySend(Result.failure(Exception("Sign in was cancelled please try again")))
            } catch (e: Exception) {
                Log.i("googleSignIn", "Error during sign in ${e.message}")
                trySend(Result.failure(e))
            }
            awaitClose { }
        }
    }

    // Helper function to hash the nonce
    private fun hashNonce(nonce: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(nonce.toByteArray())
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private suspend fun getUserData() : UserProfile? {
        return withContext(Dispatchers.IO) {
            repository.getCurrentUser()
        }
    }

    private suspend fun saveUserProfile(userProfile: UserProfile) {
        withContext(Dispatchers.IO) {
            repository.saveUserProfileToFireStore(userProfile)
        }
    }

    fun resetSignInState() {
        _state.update { LoginState() }
    }
}