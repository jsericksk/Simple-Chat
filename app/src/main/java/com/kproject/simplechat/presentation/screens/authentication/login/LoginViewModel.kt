package com.kproject.simplechat.presentation.screens.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.firebase.authentication.login.LoginUseCase
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidateEmailUseCase
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.ValidatePasswordUseCase
import com.kproject.simplechat.presentation.mapper.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    var loginState: DataState<Unit>? by mutableStateOf(null)
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                uiState = uiState.copy(email = event.email)
                validateFieldsWhenTyping()
            }
            is LoginEvent.PasswordChanged -> {
                uiState = uiState.copy(password = event.password)
                validateFieldsWhenTyping()
            }
            is LoginEvent.OnDismissErrorDialog -> {
                uiState = uiState.copy(loginError = false)
            }
        }
    }

    fun login() {
        if (hasValidationError()) {
            uiState = uiState.copy(validateFieldsWhenTyping = true)
        } else {
            uiState = uiState.copy(isLoading = true)
            viewModelScope.launch {
                val loginResult = loginUseCase(uiState.toLoginModel())
                when (loginResult) {
                    is DataState.Success -> {
                        loginState = DataState.Success()
                        uiState = uiState.copy(isLoading = false)
                    }
                    is DataState.Error -> {
                        loginResult.exception?.let { exception ->
                            uiState = uiState.copy(
                                isLoading = false,
                                loginError = true,
                                loginErrorMessage = exception.toErrorMessage()
                            )
                        }
                    }
                    else -> {
                        uiState = uiState.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun hasValidationError(): Boolean {
        val emailValidationState = validateEmailUseCase(uiState.email)
        uiState = uiState.copy(emailError = emailValidationState.toErrorMessage())
        val passwordValidationState = validatePasswordUseCase(uiState.password)
        uiState = uiState.copy(passwordError = passwordValidationState.toErrorMessage())

        val hasError = listOf(
            emailValidationState,
            passwordValidationState
        ).any { validationState ->
            validationState != ValidationState.Success
        }

        return hasError
    }

    private fun validateFieldsWhenTyping() {
        if (uiState.validateFieldsWhenTyping) {
            hasValidationError()
        }
    }
}