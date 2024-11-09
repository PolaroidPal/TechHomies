package com.example.stepcounterapp.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounterapp.settings.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
): ViewModel() {

    val userName = repository.getContact().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ""
    )

    fun signOutUser() {
        viewModelScope.launch {
            signOut()
        }
    }

    fun saveData(name: String) {
        viewModelScope.launch {
            repository.saveContact(name = name)
        }
    }

    private suspend fun signOut() {
        withContext(Dispatchers.IO) {
            repository.signOut()
        }
    }
}