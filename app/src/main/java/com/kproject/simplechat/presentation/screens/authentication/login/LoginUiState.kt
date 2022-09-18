package com.kproject.simplechat.presentation.screens.authentication.login

data class LoginUiState(
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = ""
)