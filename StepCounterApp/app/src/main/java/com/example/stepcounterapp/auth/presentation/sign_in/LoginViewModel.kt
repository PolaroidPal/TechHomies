package com.example.stepcounterapp.auth.presentation.sign_in

import androidx.lifecycle.ViewModel
import com.example.stepcounterapp.core.util.NavigationEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    // Expose login state as a state
    private val _isLoggedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    private var authStateListener: AuthStateListener = AuthStateListener { auth ->
        _isLoggedIn.value = auth.currentUser != null
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    fun navigateTo(screen: NavigationEvent) {
        _navigationEvent.value = screen
    }

    fun resetNavigation() {
        _navigationEvent.value = null
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
}