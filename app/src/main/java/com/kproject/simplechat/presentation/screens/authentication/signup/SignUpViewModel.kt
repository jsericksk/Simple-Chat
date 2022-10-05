package com.kproject.simplechat.presentation.screens.authentication.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.commom.validation.ValidationState
import com.kproject.simplechat.domain.usecase.firebase.authentication.SignUpUseCase
import com.kproject.simplechat.domain.usecase.firebase.authentication.validation.*
import com.kproject.simplechat.presentation.mapper.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val validateProfilePictureUseCase: ValidateProfilePictureUseCase,
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCase
) : ViewModel() {
    var uiState by mutableStateOf(SignUpUiState())
        private set

    var signUpState: DataState<Unit>? by mutableStateOf(null)
        private set

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.ProfilePictureChanged -> {
                uiState = uiState.copy(profilePicture = event.profilePicture)
                validateFieldsWhenTyping()
            }
            is SignUpEvent.EmailChanged -> {
                uiState = uiState.copy(email = event.email)
                validateFieldsWhenTyping()
            }
            is SignUpEvent.UsernameChanged -> {
                uiState = uiState.copy(username = event.username)
                validateFieldsWhenTyping()
            }
            is SignUpEvent.PasswordChanged -> {
                uiState = uiState.copy(password = event.password)
                validateFieldsWhenTyping()
            }
            is SignUpEvent.RepeatedPasswordChanged -> {
                uiState = uiState.copy(repeatedPassword = event.repeatedPassword)
                validateFieldsWhenTyping()
            }
            is SignUpEvent.OnDismissErrorDialog -> {
                uiState = uiState.copy(signUpError = false)
            }
        }
    }

    fun signUp() {
        if (hasValidationError()) {
            uiState = uiState.copy(validateFieldsWhenTyping = true)
        } else {
            uiState = uiState.copy(isLoading = true)
            viewModelScope.launch {
                val signUpResult = signUpUseCase(uiState.toSignUpModel())
                when (signUpResult) {
                    is DataState.Success -> {
                        signUpState = DataState.Success()
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
        val profilePictureValidationState = validateProfilePictureUseCase(uiState.profilePicture)
        uiState = uiState.copy(profilePictureError = profilePictureValidationState.toErrorMessage())
        val usernameValidationState = validateUsernameUseCase(uiState.username)
        uiState = uiState.copy(usernameError = usernameValidationState.toErrorMessage())
        val emailValidationState = validateEmailUseCase(uiState.email)
        uiState = uiState.copy(emailError = emailValidationState.toErrorMessage())
        val passwordValidationState = validatePasswordUseCase(uiState.password)
        uiState = uiState.copy(passwordError = passwordValidationState.toErrorMessage())
        val repeatedPasswordValidationState = validateRepeatedPasswordUseCase(
            uiState.password, uiState.repeatedPassword
        )
        uiState = uiState.copy(
            repeatedPasswordError = repeatedPasswordValidationState.toErrorMessage()
        )

        val hasError = listOf(
            profilePictureValidationState,
            usernameValidationState,
            emailValidationState,
            passwordValidationState,
            repeatedPasswordValidationState
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