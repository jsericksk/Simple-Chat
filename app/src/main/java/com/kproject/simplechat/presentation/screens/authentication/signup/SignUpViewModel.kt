package com.kproject.simplechat.presentation.screens.authentication.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kproject.simplechat.presentation.screens.authentication.login.LoginEvent

class SignUpViewModel : ViewModel() {
    var signUpUiState by mutableStateOf(SignUpUiState())
        private set

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.ProfileImageChanged -> {
                signUpUiState = signUpUiState.copy(profileImage = event.profileImage)
            }
            is SignUpEvent.EmailChanged -> {
                signUpUiState = signUpUiState.copy(email = event.email)
            }
            is SignUpEvent.UsernameChanged -> {
                signUpUiState = signUpUiState.copy(username = event.username)
            }
            is SignUpEvent.PasswordChanged -> {
                signUpUiState = signUpUiState.copy(password = event.password)
            }
            is SignUpEvent.RepeatedPasswordChanged -> {
                signUpUiState = signUpUiState.copy(repeatedPassword = event.repeatedPassword)
            }
        }
    }
    
}