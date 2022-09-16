package com.kproject.simplechat.presentation.screens.authentication.signup

data class SignUpUiState(
    val profileImage: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmedPassword: String = "",
)