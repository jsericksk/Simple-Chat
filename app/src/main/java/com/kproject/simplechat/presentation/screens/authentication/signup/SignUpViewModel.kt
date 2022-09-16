package com.kproject.simplechat.presentation.screens.authentication.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    var signUpUiState by mutableStateOf(SignUpUiState())
        private set

    fun onProfileImageChange(profileImage: String) {
        signUpUiState = signUpUiState.copy(profileImage = profileImage)
    }

    fun onUsernameChange(username: String) {
        signUpUiState = signUpUiState.copy(username = username)
    }

    fun onEmailChange(email: String) {
        signUpUiState = signUpUiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        signUpUiState = signUpUiState.copy(password = password)
    }

    fun onConfirmedPasswordChange(confirmedPassword: String) {
        signUpUiState = signUpUiState.copy(confirmedPassword = confirmedPassword)
    }
    
}