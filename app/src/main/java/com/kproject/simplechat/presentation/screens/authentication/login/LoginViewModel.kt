package com.kproject.simplechat.presentation.screens.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String) {
        loginUiState = loginUiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        loginUiState = loginUiState.copy(password = password)
    }
}