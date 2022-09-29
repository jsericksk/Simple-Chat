package com.kproject.simplechat.presentation.screens.authentication.signup

import com.kproject.simplechat.domain.model.authentication.SignUp
import com.kproject.simplechat.presentation.model.UiText

data class SignUpUiState(
    val profilePicture: String = "",
    val profilePictureError: UiText = UiText.HardcodedString(""),
    val email: String = "",
    val emailError: UiText = UiText.HardcodedString(""),
    val username: String = "",
    val usernameError: UiText = UiText.HardcodedString(""),
    val password: String = "",
    val passwordError: UiText = UiText.HardcodedString(""),
    val repeatedPassword: String = "",
    val repeatedPasswordError: UiText = UiText.HardcodedString(""),
    val isLoading: Boolean = false,
    val signUpError: Boolean = false,
    val signUpErrorMessage: UiText = UiText.HardcodedString(""),
    val validateFieldsWhenTyping: Boolean = false
)

fun SignUpUiState.toSignUpModel() = SignUp(profilePicture, username, email, password)