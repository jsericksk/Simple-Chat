package com.kproject.simplechat.presentation.screens.authentication.login

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object OnDismissErrorDialog : LoginEvent()
}