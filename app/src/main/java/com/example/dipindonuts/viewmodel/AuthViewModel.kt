package com.example.dipindonuts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dipindonuts.data.model.User
import com.example.dipindonuts.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    //ViewModel
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkCurrentUser()
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signIn(email, password)
            _authState.value = when {
                result.isSuccess -> {
                    _currentUser.value = result.getOrNull()
                    AuthState.Success(result.getOrNull()!!)
                }
                result.isFailure -> AuthState.Error(result.exceptionOrNull()?.message ?: "Sign in failed")
                else -> AuthState.Error("Unknown error occurred")
            }
        }
    }
    
    fun signUp(email: String, password: String, name: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.signUp(email, password, name, role)
            _authState.value = when {
                result.isSuccess -> {
                    _currentUser.value = result.getOrNull()
                    AuthState.Success(result.getOrNull()!!)
                }
                result.isFailure -> AuthState.Error(result.exceptionOrNull()?.message ?: "Sign up failed")
                else -> AuthState.Error("Unknown error occurred")
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _currentUser.value = null
            _authState.value = AuthState.Initial
        }
    }
    
    private fun checkCurrentUser() {
        val user = authRepository.getCurrentUser()
        _currentUser.value = user
        if (user != null) {
            _authState.value = AuthState.Success(user)
        }
    }
    
    fun resetAuthState() {
        _authState.value = AuthState.Initial
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
} 