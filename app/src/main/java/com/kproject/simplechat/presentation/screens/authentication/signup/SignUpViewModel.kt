package com.kproject.simplechat.presentation.screens.authentication.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.R
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.authentication.signup.SignUpUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateEmailUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidatePasswordUseCase
import com.kproject.simplechat.domain.usecase.authentication.validation.ValidateUsernameUseCase
import com.kproject.simplechat.presentation.mapper.toErrorMessage
import com.kproject.simplechat.presentation.model.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(SignUpUiState())
        private set

    var signUpState: DataState<Unit>? by mutableStateOf(null)
        private set

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.ProfileImageChanged -> {
                uiState = uiState.copy(profileImage = event.profileImage)
            }
            is SignUpEvent.EmailChanged -> {
                uiState = uiState.copy(email = event.email)
            }
            is SignUpEvent.UsernameChanged -> {
                uiState = uiState.copy(username = event.username)
            }
            is SignUpEvent.PasswordChanged -> {
                uiState = uiState.copy(password = event.password)
            }
            is SignUpEvent.RepeatedPasswordChanged -> {
                uiState = uiState.copy(repeatedPassword = event.repeatedPassword)
            }
            is SignUpEvent.OnDismissErrorDialog -> {
                uiState = uiState.copy(signUpError = false)
            }
        }
    }

    fun signUp() {
        if (!hasValidationError()) {
            uiState = uiState.copy(isLoading = true)
            viewModelScope.launch {
                val signUpResult = signUpUseCase(uiState.toSignUpModel())
                when (signUpResult) {
                    is DataState.Success -> {
                        uiState = uiState.copy(isLoading = false)
                    }
                    is DataState.Error -> {
                        signUpResult.exception?.let { exception ->
                            uiState = uiState.copy(
                                isLoading = false,
                                signUpError = true,
                                signUpErrorMessage = exception.toErrorMessage()
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
        val usernameValidationState = validateUsernameUseCase(uiState.username)
        uiState = uiState.copy(usernameError = usernameValidationState.toErrorMessage())
        val emailValidationState = validateEmailUseCase(uiState.email)
        uiState = uiState.copy(emailError = emailValidationState.toErrorMessage())
        val passwordValidationState = validatePasswordUseCase(uiState.password)
        uiState = uiState.copy(passwordError = passwordValidationState.toErrorMessage())

        if (uiState.repeatedPassword != uiState.password) {
            uiState = uiState.copy(
                repeatedPasswordError = UiText.StringResource(R.string.error_passwords_does_not_match)
            )
            return false
        } else {
            uiState = uiState.copy(
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