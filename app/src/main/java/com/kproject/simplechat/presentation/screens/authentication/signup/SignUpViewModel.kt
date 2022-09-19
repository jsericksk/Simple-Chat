package com.kproject.simplechat.presentation.screens.authentication.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kproject.simplechat.R
import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateEmailUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidatePasswordUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateUsernameUseCase
import com.kproject.simplechat.presentation.mapper.toErrorMessage
import com.kproject.simplechat.presentation.model.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : ViewModel() {
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
            is SignUpEvent.OnDismissErrorDialog -> {
                signUpUiState = signUpUiState.copy(signUpError = false)
            }
        }
    }

    fun signUp() {
        if (!hasValidationError()) {

        }
    }

    private fun hasValidationError(): Boolean {
        val usernameValidationState = validateUsernameUseCase(signUpUiState.username)
        signUpUiState = signUpUiState.copy(usernameError = usernameValidationState.toErrorMessage())
        val emailValidationState = validateEmailUseCase(signUpUiState.email)
        signUpUiState = signUpUiState.copy(emailError = emailValidationState.toErrorMessage())
        val passwordValidationState = validatePasswordUseCase(signUpUiState.password)
        signUpUiState = signUpUiState.copy(passwordError = passwordValidationState.toErrorMessage())

        if (signUpUiState.repeatedPassword != signUpUiState.password) {
            signUpUiState = signUpUiState.copy(
                repeatedPasswordError = UiText.StringResource(R.string.error_passwords_does_not_match)
            )
            return false
        } else {
            signUpUiState = signUpUiState.copy(
                repeatedPasswordError = UiText.HardcodedString("")
            )
        }

        val hasError = listOf(
            usernameValidationState,
            emailValidationState,
            passwordValidationState
        ).any { validationState ->
            validationState != ValidationState.Success
        }

        return hasError
    }
}