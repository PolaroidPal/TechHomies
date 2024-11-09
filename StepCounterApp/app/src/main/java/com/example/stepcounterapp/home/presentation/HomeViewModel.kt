package com.example.stepcounterapp.home.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcounterapp.core.domain.models.UserProfile
import com.example.stepcounterapp.home.domain.HomeRepository
import com.example.stepcounterapp.home.domain.models.StepsProgress
import com.example.stepcounterapp.home.domain.models.TotalStepsWithSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel(){

    val stepsProgress = mutableStateOf(StepsProgress())

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile
        .onStart { loadUserData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            UserProfile()
        )

    private var _state = MutableStateFlow(TotalStepsWithSession())
    val state: StateFlow<TotalStepsWithSession> = _state
        .onStart { loadInitialData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            TotalStepsWithSession()
        )

    suspend fun getTotalStepsId() : String {
        val id = viewModelScope.async {
            repository.getTotalStepsId()
        }

        return id.await()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getTotalStepsWithSession()

            getStepsProgress()

            if ((stepsProgress.value.stepsCovered >= _state.value.target_steps) && !_state.value.completed) {
                val updatedData = _state.value.copy(completed = true)

                updateData(true)

                _state.value = updatedData
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            getUserData()

            Log.i("profile", "Viewmodel: User data is ${_userProfile.value}")
        }
    }

    private suspend fun getTotalStepsWithSession() {
        withContext(Dispatchers.IO) {
            repository.getTotalStepsWithSession()
                .collect {
                    if (it != null) {
                        _state.value = it.copy(
                            completed = it.completed,
                            target_steps = it.target_steps,
                            stepsSession = it.stepsSession
                        )
                    }
                }
        }
    }

    private suspend fun getStepsProgress() {
        withContext(Dispatchers.IO) {
            val progress = repository.getStepsProgress()

            stepsProgress.value = progress
        }
    }

    private suspend fun getUserData() {
        withContext(Dispatchers.IO) {
            repository.getUser()?.let {
                _userProfile.value = it
            }
        }
    }

    private suspend fun updateData(completed: Boolean) {
        withContext(Dispatchers.IO) {
            repository.updateData(completed = completed)
        }
    }
}