package com.kproject.simplechat.presentation.screens.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.authentication.login.LoginUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateEmailUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidatePasswordUseCase
import com.kproject.simplechat.presentation.mapper.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                loginUiState = loginUiState.copy(email = event.email)
            }
            is LoginEvent.PasswordChanged -> {
                loginUiState = loginUiState.copy(password = event.password)
            }
        }
    }

    fun login() {
        val emailValidationState = validateEmailUseCase(loginUiState.email)
        loginUiState = loginUiState.copy(emailError = emailValidationState.toErrorMessage())
        val passwordValidationState = validatePasswordUseCase(loginUiState.password)
        loginUiState = loginUiState.copy(passwordError = passwordValidationState.toErrorMessage())

        val hasError = listOf(
            emailValidationState,
            passwordValidationState
        ).any { validationState ->
            validationState != ValidationState.Success
        }

        if (!hasError) {
            onLogin()
        }
    }

    private fun onLogin() {

    }
}