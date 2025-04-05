package com.yugentech.kaizen.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.yugentech.kaizen.data.auth.AuthResult
import com.yugentech.kaizen.data.auth.AuthUiState
import com.yugentech.kaizen.data.auth.UserData
import com.yugentech.kaizen.repositories.firebaseRepository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FirebaseViewModel(
    private val repository: FirebaseRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState

    init {
        val currentUser = repository.getCurrentUser()
        updateAuthState(currentUser)
    }

    fun signUp(email: String, password: String) {
        _authState.value = _authState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = repository.signUp(email, password)) {
                is AuthResult.Success -> updateAuthState(result.data)
                is AuthResult.Error -> _authState.value =
                    _authState.value.copy(isLoading = false, error = result.message)

                is AuthResult.Loading -> _authState.value = _authState.value.copy(isLoading = true)
            }
        }
    }

    fun signIn(email: String, password: String) {
        _authState.value = _authState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = repository.signIn(email, password)) {
                is AuthResult.Success -> updateAuthState(result.data)
                is AuthResult.Error -> _authState.value =
                    _authState.value.copy(isLoading = false, error = result.message)

                is AuthResult.Loading -> _authState.value = _authState.value.copy(isLoading = true)
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthUiState(isUserLoggedIn = false)
    }

    fun getGoogleSignInIntent(webClientId: String) {
        viewModelScope.launch {
            when (val result = repository.getGoogleSignInIntent(webClientId)) {
                is AuthResult.Success -> _authState.value =
                    _authState.value.copy(pendingIntent = result.data)

                is AuthResult.Error -> _authState.value =
                    _authState.value.copy(error = result.message)

                is AuthResult.Loading -> _authState.value = _authState.value.copy(isLoading = true)
            }
        }
    }

    fun handleGoogleSignInResult(data: Intent?) {
        _authState.value = _authState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = repository.handleGoogleSignInResult(data)) {
                is AuthResult.Success -> updateAuthState(result.data)
                is AuthResult.Error -> _authState.value =
                    _authState.value.copy(isLoading = false, error = result.message)

                is AuthResult.Loading -> _authState.value = _authState.value.copy(isLoading = true)
            }
        }
    }

    private fun updateAuthState(user: FirebaseUser?) {
        if (user != null) {
            val userData = UserData(
                userId = user.uid,
                username = user.displayName,
                profilePictureUrl = user.photoUrl?.toString(),
                email = user.email
            )
            _authState.value = AuthUiState(
                isUserLoggedIn = true,
                userId = user.uid,
                userData = userData
            )
        } else {
            _authState.value = AuthUiState(isUserLoggedIn = false)
        }
    }
}