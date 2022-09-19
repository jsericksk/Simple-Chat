package com.kproject.simplechat.presentation.screens.authentication.login

import com.kproject.simplechat.domain.model.authentication.Login
import com.kproject.simplechat.presentation.model.UiText

data class LoginUiState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isLoading: Boolean = false,
    val loginError: Boolean = false,
    val loginErrorMessage: UiText = UiText.HardcodedString("")
)

fun LoginUiState.toLoginModel() = Login(email, password)