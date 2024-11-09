package com.example.stepcounterapp.settings.data

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.stepcounterapp.settings.domain.SettingsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.concurrent.CancellationException
import javax.inject.Inject

val USER_KEY = stringPreferencesKey("emergency_contact")

class SettingsRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override suspend fun signOut() {
        val credentialManager = CredentialManager.create(context = context)

        try {
            firebaseAuth.signOut()

            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: CancellationException) {
            Log.i("user", "signOut: coroutine cancelled ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.i("user", "signOut: sign out error ${e.message}")
            throw e
        }
    }

    override fun getContact(): Flow<String> {
        return dataStore.data.catch { emit(emptyPreferences()) }.map {
            it[USER_KEY] ?: ""
        }
    }

    override suspend fun saveContact(name: String) {
        dataStore.edit {
            it[USER_KEY] =  name
        }
    }
}