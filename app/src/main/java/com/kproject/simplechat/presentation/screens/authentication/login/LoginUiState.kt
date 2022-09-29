package com.kproject.simplechat.presentation.screens.authentication.login

import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.presentation.model.UiText

data class LoginUiState(
    val email: String = "",
    val emailError: UiText = UiText.HardcodedString(""),
    val password: String = "",
    val passwordError: UiText = UiText.HardcodedString(""),
    val isLoading: Boolean = false,
    val loginError: Boolean = false,
    val loginErrorMessage: UiText = UiText.HardcodedString(""),
    val validateFieldsWhenTyping: Boolean = false
)

fun LoginUiState.toLoginModel() = Login(email, password)